# Microcommerce

Application Spring Boot composée de quatre modules :

| Module | Rôle | Port |
|---|---|---:|
| `product-service` | Catalogue des produits | 8900 |
| `inventory-service` | Stock des produits | 8901 |
| `order-service` | Création des commandes | 8902 |
| `gateway-service` | Point d'entrée unique | 8080 |

Chaque service possède sa propre base H2 en mémoire. Tous les appels applicatifs passent par l'API Gateway.


## Compiler l'application

Depuis la racine du projet :

```powershell
.\mvnw.cmd clean install -U
```

## Démarrer les services

Ouvrez quatre terminaux PowerShell distincts. Dans chacun, exécutez une seule commande.

### Terminal 1 - Produits

```powershell
.\mvnw.cmd -pl product-service spring-boot:run
```

### Terminal 2 - Inventaire

```powershell
.\mvnw.cmd -pl inventory-service spring-boot:run
```

### Terminal 3 - Commandes

```powershell
.\mvnw.cmd -pl order-service spring-boot:run
```

### Terminal 4 - Gateway

```powershell
.\mvnw.cmd -pl gateway-service spring-boot:run
```

Attendez le message `Started ...Application` dans chaque terminal.

## Accès aux API

Utilisez toujours le port de la gateway :

- Produits : `http://localhost:8080/products`
- Inventaire : `http://localhost:8080/inventory`
- Commandes : `http://localhost:8080/orders`
- État de la gateway : `http://localhost:8080/actuator/health`

Les ports directs 8900, 8901 et 8902 sont réservés aux communications internes et à l'administration locale.

## Tester le scénario complet avec Postman

1. Lance les quatre services.
2. Ouvre Postman.
3. Crée une collection nommée `Microcommerce`.
4. Utilise le port `8080` de la gateway pour toutes les requêtes.

### 1. Créer un produit

- Méthode : `POST`
- URL : `http://localhost:8080/products`
- Onglet `Body` : `raw`
- Format : `JSON`

```json
{
  "name": "Laptop",
  "category": "Tech",
  "description": "Portable gaming laptop"
}
```

Conserve l'`id` retourné, par exemple `1`.

### 2. Consulter le stock initial

- Méthode : `GET`
- URL : `http://localhost:8080/inventory/1`

Résultat attendu :

```json
{
  "productId": 1,
  "name": "Laptop",
  "category": "Tech",
  "quantity": 0
}
```

### 3. Fixer le stock à 5

- Méthode : `PUT`
- URL : `http://localhost:8080/inventory/1`
- Onglet `Body` : `raw`
- Format : `JSON`

```json
{
  "quantity": 5
}
```

### 4. Passer une commande de 2 unités

- Méthode : `POST`
- URL : `http://localhost:8080/orders`
- Onglet `Body` : `raw`
- Format : `JSON`

```json
{
  "productId": 1,
  "quantity": 2
}
```

### 5. Vérifier qu'il reste 3 unités

- Méthode : `GET`
- URL : `http://localhost:8080/inventory/1`

Résultat attendu : `"quantity": 3`.

### 6. Essayer une commande de 4 unités

- Méthode : `POST`
- URL : `http://localhost:8080/orders`
- Onglet `Body` : `raw`
- Format : `JSON`

```json
{
  "productId": 1,
  "quantity": 4
}
```

Postman doit afficher `400 Bad Request`. Le message retourné doit être :

```text
Stock insuffisant
```

Aucune commande ne doit être créée et le stock ne doit pas être modifié.

### 7. Vérifier que le stock reste à 3

- Méthode : `GET`
- URL : `http://localhost:8080/inventory/1`

Résultat attendu : `"quantity": 3`.

## Arrêter l'application

Dans chacun des quatre terminaux de service, appuyez sur `Ctrl+C`.

Si un service reste bloqué sur un port Windows, trouvez le processus avec :

```powershell
Get-NetTCPConnection -LocalPort 8080,8900,8901,8902 -ErrorAction SilentlyContinue |
  Select-Object LocalPort,OwningProcess
```

Puis arrêtez-le en utilisant son PID :

```powershell
Stop-Process -Id <PID> -Force
```

## Dépannage

### Le port est déjà utilisé

Arrêtez le processus occupant le port indiqué, puis relancez le service.

### Maven utilise une mauvaise version du plugin

Lancez le build depuis la racine du projet :

```powershell
.\mvnw.cmd clean install -U
```

Le plugin Spring Boot est fixé à la version `3.3.5` dans le projet.

### Le stock ne se charge pas après la création

Vérifiez que les quatre services sont démarrés dans cet ordre :

1. `product-service`
2. `inventory-service`
3. `order-service`
4. `gateway-service`

Toutes les requêtes doivent utiliser le port `8080` de la gateway.
