provider "aws" {
  region = "us-east-2"
}

# Create an ECR repository named "uptimego_api"
resource "aws_ecr_repository" "uptimego_api" {
  name = "uptimego_api"
}

# IAM role for ECS task execution
resource "aws_iam_role" "uptimego_api_execution_role" {
  name               = "uptimego_api_execution_role"
  assume_role_policy = jsonencode({
    Version   = "2012-10-17",
    Statement = [
      {
        Action    = "sts:AssumeRole",
        Effect    = "Allow",
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

# Create an ECS cluster named "uptimego-api-cluster"
resource "aws_ecs_cluster" "uptimego_api_cluster" {
  name = "uptimego-api-cluster"
}

# Security group for ECS service
resource "aws_security_group" "uptimego_api_sg" {
  name        = "uptimego_api_sg"
  description = "Security group for uptimego API"

  # Add your ingress and egress rules as needed
}

# Create an ECS task definition named "uptimego-api-task-definition"
resource "aws_ecs_task_definition" "uptimego_api_task_definition" {
  family                   = "uptimego-api-task-definition"
  container_definitions    = <<DEFINITION
[
  {
    "name": "uptimego-api-container",
    "image": "${aws_ecr_repository.uptimego_api.repository_url}:latest",
    "cpu": 256,
    "memory": 512,
    "essential": true,
    "portMappings": [
      {
        "containerPort": 80,
        "hostPort": 80
      }
    ]
    // Add other configurations as needed
  }
]
DEFINITION
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = "256"
  memory                   = "512"
  execution_role_arn       = aws_iam_role.uptimego_api_execution_role.arn
}

# Create an ECS service named "uptimego-api-service"
resource "aws_ecs_service" "uptimego_api_service" {
  name            = "uptimego-api-service"
  cluster         = aws_ecs_cluster.uptimego_api_cluster.id
  task_definition = aws_ecs_task_definition.uptimego_api_task_definition.arn
  launch_type     = "FARGATE"

  desired_count = 1

  network_configuration {
    subnets = [
      "subnet-02742491a1a6ae482",
      "subnet-05ac36642da8236ec",
      "subnet-0f618abf2862bc9c5",
      "subnet-06371a1a93044fcb4",
      "subnet-063a910db8f9815e7",
      "subnet-04a6f4189388d9ec6"
    ]
    security_groups  = [aws_security_group.uptimego_api_sg.id]
    assign_public_ip = false
  }
}
