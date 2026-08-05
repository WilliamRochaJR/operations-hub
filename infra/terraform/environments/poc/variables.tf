variable "aws_region" {
  description = "Região AWS onde a PoC será criada."
  type        = string
  default     = "us-east-1"
}

variable "project_name" {
  type    = string
  default = "operations-hub"
}

variable "environment" {
  type    = string
  default = "poc"
}

variable "vpc_cidr" {
  type    = string
  default = "10.20.0.0/16"
}

variable "kubernetes_version" {
  description = "Versão suportada pelo EKS na região escolhida."
  type        = string
  default     = "1.33"
}

variable "db_instance_class" {
  type    = string
  default = "db.t4g.micro"
}

variable "alert_email" {
  description = "E-mail opcional para confirmação do alerta de orçamento."
  type        = string
  default     = ""
}

variable "monthly_budget_usd" {
  type    = number
  default = 100
}
