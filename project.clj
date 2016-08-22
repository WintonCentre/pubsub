(defproject pubsub "0.1.0-SNAPSHOT"
  :description "A simple publish subscribe library with tests in devcards!"
  :url "http://github.com/gmp26/pubsub"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.5.3"

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.8.51"]
                 [devcards "0.2.1-7"]
                 [rum "0.10.4"]
                 [figwheel-sidecar "0.5.4-6"]               ;; remove if using 'leing figwheel devcards'
                 ]

  :plugins [;;  reinstate if not using figwheel-sidecar [lein-figwheel "0.5.3-2"]
            [lein-cljsbuild "1.1.3" :exclusions [org.clojure/clojure]]]

  :clean-targets ^{:protect false} ["resources/public/js"
                                    "target"]

  :source-paths ["src"]

  :cljsbuild {
              :builds [{:id "devcards"
                        :source-paths ["src"]
                        :figwheel { :devcards true } ;; <- note this
                        :compiler { :main       "pubsubcards.devcards" ;; <- and this
                                   :asset-path "js/compiled/devcards_out"
                                   :output-to  "resources/public/js/compiled/pubsubcards_devcards.js"
                                   :output-dir "resources/public/js/compiled/devcards_out"
                                   :source-map-timestamp true }}
                       ]}

  :figwheel { :css-dirs ["resources/public/css"] })
