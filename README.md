# stoic-garden-59244

A simple API built in Clojure

## Dependencies

- Git [git-scm.com](https://git-scm.com/)
- Java [oracle.com/java/](https://www.oracle.com/java/)
- Clojure [clojure.org](https://clojure.org/)
- Leiningen [leiningen.org](https://leiningen.org/)
- Heroku CLI [devcenter.heroku.com/articles/heroku-cli](https://devcenter.heroku.com/articles/heroku-cli)
- HTTPie (for testing) [https://httpie.io/](https://httpie.io/)

## Installation

```bash
# Clone this repository
git clone https://github.com/mes32/stoic-garden-59244.git
```

## Usage

### In the Terminal

```bash
# Start the REPL
lein repl
```

### In the REPL

```clojure
;; Load requirements
(require 'stoic-garden-59244.core)

;; Start the server
(def server (stoic-garden-59244.core/-main))
```

## Use the Endpoints Locally

#### POST /plant

Inserts a new plant into the app state. Returns status 201 if successful or status 422 if the request json is malformed.

```bash
http post http://localhost:5000/plant name="Tomatoes" form-factor="SEED" emoji="🍅" price="1.2" quantity="30"
```

```json
HTTP/1.1 201 Created
Content-Length: 0
Content-Type: application/json
Date: Sat, 18 Feb 2023 17:32:56 GMT
Server: Jetty(9.4.12.v20180830)
```

#### GET /plant

Returns a list of all plants currently in the app state.

```bash
http get http://localhost:5000/plant
```

```json
HTTP/1.1 200 OK
Content-Length: 445
Content-Type: application/json
Date: Sat, 18 Feb 2023 17:04:55 GMT
Server: Jetty(9.4.12.v20180830)

[
    {
        "emoji": "🍏",
        "form-factor": "TREE",
        "id": 1,
        "name": "Apple",
        "price": 150.0,
        "quantity": 42
    },
    {
        "emoji": "🫐",
        "form-factor": "BUSH",
        "id": 2,
        "name": "Blueberry",
        "price": 22.5,
        "quantity": 32
    },
    {
        "emoji": "🥬",
        "form-factor": "SEED",
        "id": 3,
        "name": "Bok Choy",
        "price": 1.2,
        "quantity": 64
    },
    {
        "emoji": "🥕",
        "form-factor": "SEED",
        "id": 4,
        "name": "Carrots",
        "price": 1.2,
        "quantity": 32
    },
    {
        "emoji": "🍒",
        "form-factor": "TREE",
        "id": 5,
        "name": "Cherry",
        "price": 160.0,
        "quantity": 4
    }
]
```

#### GET /plant/:id

Returns the plant with `:id` from the app state or returns a 404 status if no plant with that id exists.

```bash
http get http://localhost:5000/plant/2
```

```json
HTTP/1.1 200 OK
Content-Length: 90
Content-Type: application/json
Date: Sat, 18 Feb 2023 17:07:41 GMT
Server: Jetty(9.4.12.v20180830)

{
    "emoji": "🫐",
    "form-factor": "BUSH",
    "id": 2,
    "name": "Blueberry",
    "price": 22.5,
    "quantity": 32
}
```

#### PATCH /plant/:id

Update plant with `:id` in the app state. Requests only need to include the fields to be updated with new their new values. Returns status 200 on success. Returns status 404 if no plant with that id exists. Returns status 422 if the request json is malformed.

```bash
http patch http://localhost:5000/plant/3 quantity="62"
```

```json
HTTP/1.1 200 OK
Content-Length: 0
Date: Sat, 18 Feb 2023 17:38:28 GMT
Server: Jetty(9.4.12.v20180830)
```

#### DELETE /plant/:id

Removes all plants with `:id` from the app state. Returns status 204 on success and returns a 404 status if no plant with that id exists.

```bash
http delete http://localhost:5000/plant/4
```

```json
HTTP/1.1 204 No Content
Content-Type: application/json
Date: Sat, 18 Feb 2023 17:14:33 GMT
Server: Jetty(9.4.12.v20180830)
```

## Deploying the API to Heroku

### Heroku Setup

```bash
# 1. Login and initialize the current project for use with heroku
cd stoic-garden-59244
heroku login
heroku create

# 2. Push local changes to heroku
git push heroku main

# 3. Scale up the remote server on heroku
heroku ps:scale web=1

# 4. Open a browser window pointed at the remote server
heroku open
```

### Other Commands

```bash
# Display a live tail of the remote server logs
heroku logs --tail

# Scale down the remote server
heroku ps:scale web=0
```

## References

- [https://devcenter.heroku.com/articles/getting-started-with-clojure?singlepage=true](https://devcenter.heroku.com/articles/getting-started-with-clojure?singlepage=true)

## License

The code in this repository is licensed under the [MIT License](./LICENSE).
