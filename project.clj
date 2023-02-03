(defproject stoic-garden-59244 "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [compojure "1.6.1"]
                 [ring/ring-jetty-adapter "1.7.1"]]
  :main ^:skip-aot stoic-garden-59244.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
