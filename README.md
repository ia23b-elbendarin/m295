# Nour Elbendari AE23b  
M295_Backend / M295_Rezepte_API

---

## Beschreibung
Dieses Projekt wurde im Modul M295 – Applikationsentwicklung Backend umgesetzt. Es handelt sich um eine Web-API, mit der man Rezepte und deren Zutaten verwalten kann.
Die Anwendung basiert auf einer klassischen 1:n-Beziehung:
Ein Rezept enthält mehrere Zutaten, und jede Zutat gehört genau zu einem Rezept.

Funktionen:
> -Die API stellt folgende Funktionen zur Verfügung:

CRUD-Operationen für:

> -Rezepte
> -Zutaten
---
Suchfunktionen:

> -Nach Namen von Rezepten oder Zutaten
> -Nach vegetarischen Rezepten
> -Nach Rezepten, die vor einem bestimmten Datum erfasst wurden
> -Existenzprüfung für Rezepte oder Zutaten
> -Massenspeicherung und -löschung von mehreren Einträgen gleichzeitig
---
Validierung: Diese Regeln sorgen für saubere und überprüfte Daten.

| Feld            | Regel                            |
| --------------- | -------------------------------- |
| Dauer           | Muss mindestens 1 Minute sein    |
| Bewertung       | Muss zwischen 0.0 und 5.0 liegen |
| Beschreibung    | Muss 10–500 Zeichen lang sein    |
| Erfassungsdatum | Darf nicht in der Zukunft liegen |


---
Sicherheit:
Die Anwendung verwendet Spring Security mit zwei Rollen:

> -user: darf nur lesen (GET)
> -admin: darf lesen, erstellen, ändern und löschen (GET, POST, PUT, DELETE)
---
Mit Swagger (OpenAPI 3.1) wird die API automatisch dokumentiert und kann direkt im Browser getestet werden.

Dort sieht man:
> -Welche Eingaben erlaubt sind
> -Welche Antworten zurückgegeben werden
> -Ob die Eingaben gültig sind

---

## Visuals

### 1. Datenbankdiagramm (ERD)

![ERD](https://github.com/user-attachments/assets/bba263ea-3e54-4a67-b355-73203bfe49f0)

Die Datenbank besteht aus zwei Entitäten:  
> - **Rezept** (Haupttabelle mit 7 Attributen: `id`, `name`, `beschreibung`, `dauer_in_minuten`, `bewertung`, `ist_vegetarisch`, `erfasst_am`)  
> - **Zutat** (Fremdtabelle mit 5 Attributen: `id`, `name`, `menge`, `einheit`, `rezept_id`)  

**Beziehung und Fremdschlüssel**  
> Jede **Zutat** gehört zu genau einem **Rezept** (`rezept_id`)  
> Ein **Rezept** kann mehrere **Zutaten** enthalten (1:n-Beziehung)

---

### 2. Klassendiagramm

![image](https://github.com/user-attachments/assets/653d3e68-7aef-4786-9ba3-93f2a2036daa)

---

### 3. Screenshot Testdurchführung

![Screenshot 1](https://github.com/user-attachments/assets/69cb944f-cdb1-4d94-8005-8e1adbf9bf40)  
![Screenshot 2](https://github.com/user-attachments/assets/bc57af00-6903-49a8-a4a0-a8593b2de116)  
![Screenshot 3](https://github.com/user-attachments/assets/33731b20-bf35-4171-a621-133ec29717a1)  
![Screenshot 4](https://github.com/user-attachments/assets/7b7b3658-93fe-474b-bed5-99d3795d25bc)

---

### 4. Screenshot Testdurchführung (Coverage)

![Coverage](https://github.com/user-attachments/assets/002adab5-c492-4337-b7ce-a8153b0c8ec5)

---

## Testdurchführung

- Repository-Tests mit `@SpringBootTest`
- Controller-Tests mit `MockMvc`
- Testabdeckung geprüft mit IntelliJ Code Coverage

---

## Validierungsregeln

> Eingaben werden mit Jakarta Bean Validation geprüft:

| Feld             | Typ         | Regel                                      | Annotation(en)                             | Nutzung / Zweck                                                                    |
| ---------------- | ----------- | ------------------------------------------ | ------------------------------------------ | ---------------------------------------------------------------------------------- |
| `dauerInMinuten` | `int`       | Muss mindestens 1 Minute betragen          | `@Min(1)`                                  | Verhindert unrealistische Zubereitungszeiten (z. B. 0 Minuten)                     |
| `bewertung`      | `double`    | Muss zwischen 0.0 und 5.0 liegen           | `@DecimalMin("0.0")`, `@DecimalMax("5.0")` | Bewertungsbereich begrenzen, z. B. für Sternebewertungen                           |
| `erfasstAm`      | `LocalDate` | Darf nicht in der Zukunft liegen           | `@PastOrPresent`                           | Sichert, dass Rezepte nicht versehentlich mit zukünftigem Datum gespeichert werden |
| `menge`          | `double`    | Muss mindestens 0.1 betragen               | `@DecimalMin("0.1")`                       | Stellt sicher, dass jede Zutat eine sinnvolle Menge hat                            |
| `beschreibung`   | `String`    | Muss zwischen 10 und 500 Zeichen lang sein | `@Size(min = 10, max = 500)`               | Verhindert zu kurze oder zu ausschweifende Beschreibungen                          |
| `name (Zutat)`   | `String`    | Nur Buchstaben, Leerzeichen, Bindestriche  | `@Pattern(...)` *(siehe unten)*            | Sorgt für saubere Namen wie „Tomate“, „Grüne Paprika“ usw.                         |

Die name-Validierung für Zutaten verwendet:
@Pattern(regexp = "^[A-Za-zÄÖÜäöüß\\s-]+$", message = "Name darf nur Buchstaben und Leerzeichen enthalten")

## Berechtigungsmatrix

| Aktion                               | HTTP-Methode | Endpoint                          | USER | ADMIN |
| ------------------------------------ | ------------ | --------------------------------- | ---- | ----- |
| Alle Rezepte anzeigen                | `GET`        | `/api/rezepte`                    | ✅    | ✅     |
| Einzelnes Rezept anzeigen            | `GET`        | `/api/rezepte/{id}`               | ✅    | ✅     |
| Rezept erstellen                     | `POST`       | `/api/rezepte`                    | ✅    | ✅     |
| Rezept ändern                        | `PUT`        | `/api/rezepte/{id}`               | ❌    | ✅     |
| Rezept löschen                       | `DELETE`     | `/api/rezepte/{id}`               | ❌    | ✅     |
| Rezepte vor bestimmtem Datum löschen | `DELETE`     | `/api/rezepte/vorDatum?datum=...` | ❌    | ✅     |
| Zutat zu Rezept anzeigen             | `GET`        | `/api/zutaten/byRezeptId?id=...`  | ✅    | ✅     |
| Zutat erstellen                      | `POST`       | `/api/zutaten`                    | ✅    | ✅     |
| Zutat bearbeiten                     | `PUT`        | `/api/zutaten/{id}`               | ❌    | ✅     |
| Zutat löschen                        | `DELETE`     | `/api/zutaten/{id}`               | ❌    | ✅     |


## OpenAPI-Dokumentation der Services (Ressourcen)

Die OpenAPI-Dokumentation ist erreichbar über:  
📍 `http://localhost:8080/swagger-ui/index.html`  

Die API bietet Endpunkte für:
- `GET /api/rezepte`
- `POST /api/rezepte`
- `DELETE /api/rezepte/vorDatum`
- `GET /api/zutaten/byRezeptId`


---

##  OpenAPI-Dokumentation (YAML-Format)

```yaml
openapi: 3.1.0
info:
  title: OpenAPI definition
  version: v0
servers:
- url: http://localhost:8080
  description: Generated server url
paths:
  /api/zutaten/{id}:
    get:
      tags:
      - zutat-controller
      operationId: findById
      parameters:
      - name: id
        in: path
        required: true
        schema:
          type: integer
          format: int64
      responses:
        "200":
          description: OK
          content:
            '*/*':
              schema:
                $ref: "#/components/schemas/Zutat"
    put:
      tags:
      - zutat-controller
      operationId: update
      parameters:
      - name: id
        in: path
        required: true
        schema:
          type: integer
          format: int64
      requestBody:
        content:
          application/json:
            schema:
              $ref: "#/components/schemas/Zutat"
        required: true
      responses:
        "200":
          description: OK
          content:
            '*/*':
              schema:
                $ref: "#/components/schemas/Zutat"
    delete:
      tags:
      - zutat-controller
      operationId: deleteById
      parameters:
      - name: id
        in: path
        required: true
        schema:
          type: integer
          format: int64
      responses:
        "200":
          description: OK
  /api/rezepte/{id}:
    get:
      tags:
      - rezept-controller
      operationId: getById
      parameters:
      - name: id
        in: path
        required: true
        schema:
          type: integer
          format: int64
      responses:
        "200":
          description: OK
          content:
            '*/*':
              schema:
                $ref: "#/components/schemas/Rezept"
    put:
      tags:
      - rezept-controller
      operationId: updateRezept
      parameters:
      - name: id
        in: path
        required: true
        schema:
          type: integer
          format: int64
      requestBody:
        content:
          application/json:
            schema:
              $ref: "#/components/schemas/Rezept"
        required: true
      responses:
        "200":
          description: OK
          content:
            '*/*':
              schema:
                $ref: "#/components/schemas/Rezept"
    delete:
      tags:
      - rezept-controller
      operationId: deleteById_1
      parameters:
      - name: id
        in: path
        required: true
        schema:
          type: integer
          format: int64
      responses:
        "200":
          description: OK
# ... TRUNCATED FOR BREVITY ...
components:
  schemas:
    Rezept:
      type: object
      properties:
        id:
          type: integer
          format: int64
        name:
          type: string
          minLength: 1
        beschreibung:
          type: string
          maxLength: 500
          minLength: 10
        dauerInMinuten:
          type: integer
          format: int32
          minimum: 1
        istVegetarisch:
          type: boolean
        bewertung:
          type: number
          format: double
          maximum: 5.0
          minimum: 0.0
        erfasstAm:
          type: string
          format: date
        zutaten:
          type: array
          items:
            $ref: "#/components/schemas/Zutat"
    Zutat:
      type: object
      properties:
        id:
          type: integer
          format: int64
        name:
          type: string
          minLength: 1
          pattern: "^[A-Za-zÄÖÜäöüß\\s-]+$"
        menge:
          type: number
          format: double
          minimum: 0.1
        einheit:
          type: string
          minLength: 1
        rezept:
          $ref: "#/components/schemas/Rezept"
```



## Autor

**Nour Elbendari**  
Email: elbendarin@bzz.ch
Klasse AE23b

---

## Zusammenfassung / Bemerkungen

Bei mir haben die Umgebungsvariablen nicht richtig funktioniert. Ich konnte den Fehler zwar finden, aber es war mir lieber, es so zu machen, wie ich es jetzt habe.  
