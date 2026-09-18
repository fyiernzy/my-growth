---
updated: 2025-07-30T17:12:50.816+08:00
edited_seconds: 563
---
# `.gitlab-ci.yml 
---
## Official Version

```yml
# %%% Include everything to make it become a collection of pipeline logic.
# %%% - local: is a GitLab CI keyword that tells GitLab to include another YAML file from the same repository.
# ??? - So does it mean that if I have multiple cicd logic I can define them in seperate file and include them all at once?
include:
  - local: .gitlab/ci_templates/.job.gitlab-ci.yml
  - local: .gitlab/backend/.my-batch-ws.gitlab-ci.yml
  - local: .gitlab/backend/.my-modular-ws.gitlab-ci.yml
  - local: .gitlab/frontend/.my-internal-api.gitlab-ci.yml
  - local: .gitlab/frontend/.my-public-api.gitlab-ci.yml
  - local: .gitlab/frontend/.my-web-backoffice.gitlab-ci.yml
  - local: .gitlab/frontend/.my-web-ifast-pay.gitlab-ci.yml

# ??? What does it mean by the MANIFEST_BRANCH?
# $$$ MANIFEST_BRANCH is a custom variable used in the deploy_gitops_job, telling the GitOps deployment (ArgoCD) which branch to deploy from.

# ??? If I wish to add one more rule, then set CI_COMMIT_BRANCH == 'feature/checkstyle' && $CI_PIPELINE_SOURCE == 'push'?
workflow:
  rules:
    # Rule 1: Preview Environment (Branch Push)
    - if: '$CI_COMMIT_BRANCH == "preview" && $CI_PIPELINE_SOURCE == "push"'
      variables:
        ENV: "dev"
        MANIFEST_BRANCH: "feature/dev"
      when: always

    # Rule 2: UAT Environment
    - if: '$CI_COMMIT_BRANCH == $CI_DEFAULT_BRANCH && $CI_PIPELINE_SOURCE == "push"'
      variables:
        ENV: "uat"
        MANIFEST_BRANCH: "feature/dev"
      when: always

    # Rule 3: Hotfix Environment
    - if: '$CI_COMMIT_BRANCH =~ /^hotfix\// && $CI_PIPELINE_SOURCE == "push"'
      variables:
        ENV: "hotfix"
        MANIFEST_BRANCH: $CI_COMMIT_BRANCH
      when: always

    # Rule 4: Prod Environment
    - if: '$CI_COMMIT_BRANCH == "master" && $CI_PIPELINE_SOURCE == "push"'
      variables:
        ENV: "prod"
        MANIFEST_BRANCH: "master"
      when: always

    # Rule 5: Block all other pipelines
    - when: never
```

## Temporary Version

```yml
include:
  - local: .gitlab/ci_templates/checkstyle.gitlab-ci.yml
	rules:
	  - if: '$CI_COMMIT_BRANCH == "feature/checkstyle"'

  - local: .gitlab/ci_templates/.job.gitlab-ci.yml
    rules:
      - if: '$CI_COMMIT_BRANCH != "feature/checkstyle"'

  - local: .gitlab/backend/.my-batch-ws.gitlab-ci.yml
    rules:
      - if: '$CI_COMMIT_BRANCH != "feature/checkstyle"'

  - local: .gitlab/backend/.my-modular-ws.gitlab-ci.yml
    rules:
      - if: '$CI_COMMIT_BRANCH != "feature/checkstyle"'

  - local: .gitlab/frontend/.my-internal-api.gitlab-ci.yml
    rules:
      - if: '$CI_COMMIT_BRANCH != "feature/checkstyle"'

  - local: .gitlab/frontend/.my-public-api.gitlab-ci.yml
	rules:
      - if: '$CI_COMMIT_BRANCH != "feature/checkstyle"'

  - local: .gitlab/frontend/.my-web-backoffice.gitlab-ci.yml
	rules:
	      - if: '$CI_COMMIT_BRANCH != "feature/checkstyle"'

  - local: .gitlab/frontend/.my-web-ifast-pay.gitlab-ci.yml
	rules:
		  - if: '$CI_COMMIT_BRANCH != "feature/checkstyle"'

workflow:
  rules:
	  - if: '$CI_COMMIT_BRANCH == "feature/checkstyle" && $CI_PIPELINE_SOURCE == "push"'
	    when: always

    # Rule 1: Preview Environment (Branch Push)
    - if: '$CI_COMMIT_BRANCH == "preview" && $CI_PIPELINE_SOURCE == "push"'
      variables:
        ENV: "dev"
        MANIFEST_BRANCH: "feature/dev"
      when: always

    # Rule 2: UAT Environment
    - if: '$CI_COMMIT_BRANCH == $CI_DEFAULT_BRANCH && $CI_PIPELINE_SOURCE == "push"'
      variables:
        ENV: "uat"
        MANIFEST_BRANCH: "feature/dev"
      when: always

    # Rule 3: Hotfix Environment
    - if: '$CI_COMMIT_BRANCH =~ /^hotfix\// && $CI_PIPELINE_SOURCE == "push"'
      variables:
        ENV: "hotfix"
        MANIFEST_BRANCH: $CI_COMMIT_BRANCH
      when: always

    # Rule 4: Prod Environment
    - if: '$CI_COMMIT_BRANCH == "master" && $CI_PIPELINE_SOURCE == "push"'
      variables:
        ENV: "prod"
        MANIFEST_BRANCH: "master"
      when: always

    # Rule 5: Block all other pipelines
    - when: never
```
# `./.gitlab/ci_templates/.job.gitlab-ci.yml`
---
```yml
stages:
  - build
  - deploy

# Define variable for harbor image tag name
variables:
  IMAGE_TAG: "$CI_COMMIT_REF_SLUG-$CI_COMMIT_SHORT_SHA"

# $$$ These are reusable job templates. Since their names start with a dot, they won't run by themselves.
.build_scan_push_job:
  # ??? What does it mean by stage: build? Any other options available?
  # $$$ This tells GitLab when the job should run in the pipeline.
  stage: build
  # ??? What is the use of tag?
  # $$$ This matches the job to a GitLab Runner that has the same tag. Think of runners like servers waiting for CI jobs.
  tags:
    - my-ifast-pay-build-rke2
  # ??? How to understand the image name format? Does harbor-poc has some special meaning
  # $$$ `harbor-poc.ifastcorp.com`: hostname of your private registry.
  
  # ??? What is Kaniko and tar and why do we need to use Kaniko instead of using Docker image build? So krane is also parts of the Kanifo?
  # $$$ Kaniko is a a tool to build container images in Kubernetes/CI environments without Docker. Works in rootless environments and doesn’t require a Docker daemon
 
  # ??? Understand line by line?
  image:
    name: harbor-poc.ifastcorp.com/ifast-hq/ifast-container-toolkit:v0.3
    entrypoint: [""]
  variables:
    IMAGE_NAME: "${CI_REGISTRY}/ifast-my/my-ifast-pay/${APP_IMAGE_NAME}"
    IMAGE_NAME_2: "${CI_REGISTRY_LIVE}/ifast-my/my-ifast-pay/${APP_IMAGE_NAME}"
  allow_failure: false
  script:
    - mkdir -p /kaniko/.docker
    - echo "$DOCKER_AUTH_CONFIG" > /kaniko/.docker/config.json
    - /kaniko/executor
      --context $CI_PROJECT_DIR
      --dockerfile $DOCKERFILE_PATH
      --destination "${IMAGE_NAME}:${IMAGE_TAG}"
      --no-push
      --force
      --tarPath $CI_PROJECT_DIR/image.tar
    - krane auth login -u $CI_REGISTRY_USER -p $CI_REGISTRY_SECRET $CI_REGISTRY
    - krane push $CI_PROJECT_DIR/image.tar "${IMAGE_NAME}:${IMAGE_TAG}"
    - krane auth login -u $CI_REGISTRY_USER_MY -p $CI_REGISTRY_SECRET_MY $CI_REGISTRY_LIVE
    - krane push $CI_PROJECT_DIR/image.tar "${IMAGE_NAME_2}:${IMAGE_TAG}"

.deploy_gitops_job:
  stage: deploy
  tags:
    - my-ifast-pay-build-rke2
  variables:
    # ??? What does the GIT STRATEGY means?
    GIT_STRATEGY: none
    ARGO_APP_FILE: my-ifast-pay-${NAMESPACE}/overlays/${ENV}/my-ifast-pay-${APP_IMAGE_NAME}.yaml
  # ??? What happens here?
  retry:
    max: 2
    when: script_failure
  image: harbor-poc.ifastcorp.com/ifast-hq/ifast-gitops:latest
  # ??? What happens here?
  before_script:
    - git clone https://my-ifast-pay-gitops:${GITOPS_TOKEN}@gitlab.ifastcorp.com/infra/gitops/my-ifast-pay-gitops.git
    - git config --global user.email "my-ifast-pay@gitlab.ifastcorp.com"
    - git config --global user.name "my-ifast-pay"
    - cd my-ifast-pay-gitops
    - git checkout -B master
  script:
    - echo "CI_COMMIT_BRANCH=${CI_COMMIT_BRANCH}"
    - echo "ARGO_APP_FILE=${ARGO_APP_FILE}"
    - 'sed -i "s#targetRevision:.*#targetRevision: ${MANIFEST_BRANCH}#g" ${ARGO_APP_FILE}'
    - echo "APP_IMAGE_NAME=${APP_IMAGE_NAME}"
    - echo "IMAGE_TAG=${IMAGE_TAG}"
    - sed -i "s#ifast-my/my-ifast-pay/${APP_IMAGE_NAME}:.*#ifast-my/my-ifast-pay/${APP_IMAGE_NAME}:${IMAGE_TAG}#g" ${ARGO_APP_FILE}
    - cat ${ARGO_APP_FILE}
    - 'git commit -am "GitOps: Promote ${APP_NAME} to ${ENV} with tag ${IMAGE_TAG} IssueID: GITOPS"'
    - git pull origin master --rebase
    - git push origin master
```

## Temporary Version

```yml
# .gitlab/ci_templates/checkstyle.gitlab-ci.yml
stages:
  - validate

checkstyle:
  stage: validate
  tags:
    - my-ifast-pay-build-rke2
  image: gradle:8.9-jdk17
  variables:
    GRADLE_USER_HOME: .gradle
  interruptible: true
  script:
    - gradle --no-daemon checkstyleMain checkstyleTest
  cache:
	  key: "$CI_PROJECT_ID-$CI_COMMIT_REF_SLUG-gradle-checkstyle"
	  policy: pull-push
	  paths:
	    - .gradle/wrapper
	    - .gradle/caches
  artifacts:
    when: always
    expire_in: 1 hour  
    paths:
      - '**/build/reports/checkstyle/*'
  rules:
    - if: '$CI_COMMIT_BRANCH == "feature/checkstyle" && $CI_PIPELINE_SOURCE == "push"'
      when: on_success
```

# `./.gitlab/backend/.my-modular-ws.gitlab-ci.yml`
---
```yml
.my_ifast_pay_modular:
  variables:
    APP_NAME: my-modular-ws
    APP_IMAGE_NAME: modular
    NAMESPACE: backend
    DOCKERFILE_PATH: $CI_PROJECT_DIR/my-ifast-pay-backend/my-modular-ws/Dockerfile

# Define modular changes path
.modular_changes: &modular_changes
  - "my-ifast-pay-backend/my-modular-ws/**/*"
  - "my-ifast-pay-backend/util/**/*"

# Define modular rules
.modular_rules: &modular_rules
  - if: $CI_COMMIT_BRANCH == "preview"
    changes: *modular_changes
    when: always
  - changes: *modular_changes
    when: manual

## Build and push to Harbor Registry
build_scan_push_my_ifast_pay_modular:
  extends: [.build_scan_push_job, .my_ifast_pay_modular]
  rules: *modular_rules

## Update Manifest for GitOps
deploy_my_ifast_pay_modular:
  extends: [.deploy_gitops_job, .my_ifast_pay_modular]
  needs: [job: "build_scan_push_my_ifast_pay_modular"]
  rules: *modular_rules
```