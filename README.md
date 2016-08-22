# A Publish/Subscribe library

###Installation

This library is under development and must be installed from a local copy rather than clojars.

Clone the repository to a `pubsub` directory, then:
```
cd pubsub
lein install
```

#### Current version
[ pubsub "0.1.0-SNAPSHOT" ]

## Usage

See `src/examples/logging.cljs`

### Start a REPL

The project.clj is configured to run inside an Intellij REPL. [See details here](https://github.com/bhauman/lein-figwheel/wiki/Running-figwheel-in-a-Cursive-Clojure-REPL): 

If you prefer to run tests using `lein figwheel devcards` from the command line, follow the comments in project.clj to remove figwheel-sidecar references.


#### Running examples

Start the REPL and visit http://localhost:3449/index.html.
You will find example output in the browser console.

#### Running tests in devcards

Start the REPL, then `switch-to-build devcards` and visit http://localhost:3449/cards.html