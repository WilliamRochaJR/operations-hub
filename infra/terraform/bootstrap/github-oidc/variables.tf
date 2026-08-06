variable "aws_region" {
  type    = string
  default = "us-east-1"
}

variable "github_repository" {
  type    = string
  default = "WilliamRochaJR/operations-hub"
}

variable "github_environment" {
  type    = string
  default = "poc"
}

variable "role_name" {
  type    = string
  default = "operations-hub-github-poc"
}
