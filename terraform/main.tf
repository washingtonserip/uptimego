terraform {
  cloud {
    organization = "uptimego"

    workspaces {
      name = "uptimego-api"
    }
  }
}

variable "region" {
  description = "AWS region"
  default     = "us-east-2"
}

variable "cpu" {
  description = "CPU value for ECS task definition"
  default     = "256"
}

variable "memory" {
  description = "Memory value for ECS task definition"
  default     = "512"
}

provider "aws" {
  region = var.region
}

resource "aws_vpc" "my_vpc" {
  cidr_block       = "10.0.0.0/16"
  enable_dns_support = true
  enable_dns_hostnames = true
  tags = {
    Name = "my_vpc"
  }
}

resource "aws_subnet" "my_subnet" {
  vpc_id                  = aws_vpc.my_vpc.id
  cidr_block              = "10.0.1.0/24"
  availability_zone       = "us-east-2a"
}

resource "aws_ecr_repository" "uptimego_api" {
  name = "uptimego_api"
}

resource "aws_iam_role" "uptimego_api_execution_role" {
  name = "uptimego_api_execution_role"
  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Action = "sts:AssumeRole",
        Effect = "Allow",
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        },
      },
    ]
  })
}

resource "aws_iam_role_policy_attachment" "ecs_task_execution_role_policy" {
  role       = aws_iam_role.uptimego_api_execution_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

resource "aws_ecs_cluster" "uptimego_api_cluster" {
  name = "uptimego-api-cluster"
}

resource "aws_security_group" "uptimego_api_sg" {
  name        = "uptimego_api_sg"
  description = "Security group for uptimego API"
  vpc_id      = aws_vpc.my_vpc.id # Add this line to associate with the VPC

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"] // Adjust as needed
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Environment = "production"
    Project     = "uptimego"
  }
}

resource "aws_ecs_task_definition" "uptimego_api_task_definition" {
  family                   = "uptimego-api-task-definition"
  container_definitions    = <<DEFINITION
[
  {
    "name": "uptimego-api-container",
    "image": "${aws_ecr_repository.uptimego_api.repository_url}:latest",
    "cpu": ${var.cpu},
    "memory": ${var.memory},
    "essential": true,
    "portMappings": [
      {
        "containerPort": 80,
        "hostPort": 80
      }
    ]
  }
]
DEFINITION
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = var.cpu
  memory                   = var.memory
  execution_role_arn       = aws_iam_role.uptimego_api_execution_role.arn
}

resource "aws_ecs_service" "uptimego_api_service" {
  name            = "uptimego-api-service"
  cluster         = aws_ecs_cluster.uptimego_api_cluster.id
  task_definition = aws_ecs_task_definition.uptimego_api_task_definition.arn
  launch_type     = "FARGATE"

  desired_count = 1

  network_configuration {
    subnets          = [aws_subnet.my_subnet.id] # Direct reference to the subnet ID
    security_groups  = [aws_security_group.uptimego_api_sg.id]
    assign_public_ip = false
  }
}
