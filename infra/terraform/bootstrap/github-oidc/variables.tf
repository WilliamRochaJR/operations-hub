variable "aws_region" {
  type    = string
  default = "us-east-1"
}

variable "github_environment" {
  type    = string
  default = "poc"
}

variable "github_oidc_subject" {
  description = "Subject OIDC exato emitido pelo GitHub para o repositorio e environment autorizados"
  type        = string
  default     = "repo:WilliamRochaJR@38361127/operations-hub@1323559421:environment:poc"

  validation {
    condition     = startswith(var.github_oidc_subject, "repo:") && endswith(var.github_oidc_subject, ":environment:${var.github_environment}")
    error_message = "github_oidc_subject deve identificar um repositorio e terminar com o GitHub Environment configurado."
  }
}

variable "role_name" {
  type    = string
  default = "operations-hub-github-poc"
}
