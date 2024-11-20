# Architecture

![eShopOnContainers Architecture](https://github.com/user-attachments/assets/c6a6976b-d344-41ca-ab82-05b3588ed412)

### Sync/Async:
US1: Er is geen communicatie benodigd naar meerdere microservices.
US2: Er is geen communicatie benodigd naar meerdere microservices.
US3: Er is geen communicatie benodigd naar meerdere microservices.

US4: Synchrone communicatie via OpenFeign benodigd om de reacties van een nieuwsartikel op te halen.
US5: Er is geen communicatie benodigd naar meerdere microservices.

US7: Synchrone communicatie via OpenFeign benodigd om de nieuwsartikelen die klaar staan voor review op te halen.
US8: Asynchrone communicatie via RabbitMQ benodigd om de toelatingsnotificaties uit te sturen na een review.
US9: Synchrone communicatie via OpenFeign benodigd om de nieuwsartikelen op te slaan met reviewstatus & -melding.

US10: Synchrone communicatie via OpenFeign bennodigd om het specifieke artikel op te halen & reacties to te voegen.
US11: Synchrone communicatie via OpenFeign benodigd om het specifieke artikel met reacties op te halen.
US12: Synchrone communicatie via OpenFeign benodigd om het specifieke artikel met reacties op te halen & op te slaan.
