.PHONY: install build test up down logs infra-validate

install:
	npm install

build:
	npm run build
	mvn -q -f microservices/orders-service/pom.xml package
	mvn -q -f microservices/audit-service/pom.xml package

test:
	npm test
	mvn -q -f microservices/orders-service/pom.xml test
	mvn -q -f microservices/audit-service/pom.xml test

up:
	docker compose up --build

down:
	docker compose down

logs:
	docker compose logs -f

infra-validate:
	terraform fmt -check -recursive infra/terraform
	terraform -chdir=infra/terraform/environments/poc init -backend=false
	terraform -chdir=infra/terraform/environments/poc validate
	docker run --rm -v "$(CURDIR):/workspace" -w /workspace alpine/helm:3.18.6 lint infra/helm/operations-hub
