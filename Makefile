all:
	./gradlew run

db:
	cd ./mimgrdb; \
	docker compose down; \
	docker compose up

dbd:
	cd ./mimgrdb; \
	docker compose down; \
	docker compose up -d
