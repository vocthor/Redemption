# REDEMPTION

# TODO

## Loop
- Support multiple gamecontroller by gameserver (ou PAS ? => BEAUCOUP plus simple) 
- Si une game par serveur (?) -> fusionner StartGameEvent et ConnectGameEvent en 1, qui start à la volée si besoin

## Services
- Faire un service à injecter auto pour éviter de se trimballer gamecontroller partout
- => Spring Boot ?

## Clients
- RETRAVAILLER EventEncoder + quoi qu'on renvoie à GMS2
- Retour vers client quand event added to GameController queue
