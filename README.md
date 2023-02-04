# stoic-garden-59244

A simple API built in Clojure

## Dependencies

- Git [git-scm.com](https://git-scm.com/)
- Java [oracle.com/java/](https://www.oracle.com/java/)
- Clojure [clojure.org](https://clojure.org/)
- Leiningen [leiningen.org](https://leiningen.org/)
- Heroku CLI [devcenter.heroku.com/articles/heroku-cli](https://devcenter.heroku.com/articles/heroku-cli)

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
