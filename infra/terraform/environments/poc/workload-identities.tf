locals {
  msk_topic_arn = "${replace(aws_msk_serverless_cluster.this.arn, ":cluster/", ":topic/")}/orders.events.v1"
  msk_group_arn = "${replace(aws_msk_serverless_cluster.this.arn, ":cluster/", ":group/")}/audit-service"
}

resource "aws_iam_role" "orders_service" {
  name               = "${local.name}-orders-service"
  assume_role_policy = data.aws_iam_policy_document.pod_identity_assume_role.json
}

resource "aws_iam_role_policy" "orders_service_msk" {
  name = "msk-producer"
  role = aws_iam_role.orders_service.id
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect   = "Allow"
        Action   = ["kafka-cluster:Connect", "kafka-cluster:DescribeCluster"]
        Resource = aws_msk_serverless_cluster.this.arn
      },
      {
        Effect   = "Allow"
        Action   = ["kafka-cluster:CreateTopic"]
        Resource = [aws_msk_serverless_cluster.this.arn, local.msk_topic_arn]
      },
      {
        Effect   = "Allow"
        Action   = ["kafka-cluster:DescribeTopic", "kafka-cluster:WriteData"]
        Resource = local.msk_topic_arn
      }
    ]
  })
}

resource "aws_iam_role" "audit_service" {
  name               = "${local.name}-audit-service"
  assume_role_policy = data.aws_iam_policy_document.pod_identity_assume_role.json
}

resource "aws_iam_role_policy" "audit_service_msk" {
  name = "msk-consumer"
  role = aws_iam_role.audit_service.id
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect   = "Allow"
        Action   = ["kafka-cluster:Connect", "kafka-cluster:DescribeCluster"]
        Resource = aws_msk_serverless_cluster.this.arn
      },
      {
        Effect   = "Allow"
        Action   = ["kafka-cluster:DescribeTopic", "kafka-cluster:ReadData"]
        Resource = local.msk_topic_arn
      },
      {
        Effect   = "Allow"
        Action   = ["kafka-cluster:AlterGroup", "kafka-cluster:DescribeGroup"]
        Resource = local.msk_group_arn
      }
    ]
  })
}

resource "aws_eks_pod_identity_association" "orders_service" {
  cluster_name    = module.eks.cluster_name
  namespace       = "operations-hub"
  service_account = "orders-service"
  role_arn        = aws_iam_role.orders_service.arn
}

resource "aws_eks_pod_identity_association" "audit_service" {
  cluster_name    = module.eks.cluster_name
  namespace       = "operations-hub"
  service_account = "audit-service"
  role_arn        = aws_iam_role.audit_service.arn
}
