output "aws_account_id" { value = data.aws_caller_identity.current.account_id }
output "aws_region" { value = var.aws_region }
output "cluster_name" { value = module.eks.cluster_name }
output "ecr_repository_urls" { value = { for key, repository in aws_ecr_repository.application : key => repository.repository_url } }
output "rds_endpoint" { value = aws_db_instance.postgres.address }
output "database_secret_arn" { value = aws_db_instance.postgres.master_user_secret[0].secret_arn }
output "msk_cluster_arn" { value = aws_msk_serverless_cluster.this.arn }
output "msk_bootstrap_brokers_sasl_iam" { value = data.aws_msk_bootstrap_brokers.this.bootstrap_brokers_sasl_iam }
output "configure_kubectl" { value = "aws eks update-kubeconfig --region ${var.aws_region} --name ${module.eks.cluster_name}" }
