(ns stoic-garden-59244.core
  (:require [cheshire.core :as json]
            [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [ring.adapter.jetty :as jetty]
            [environ.core :refer [env]]))

(def plants
  [{:id 1
    :name "Apple"
    :form-factor :TREE
    :emoji "🍏"
    :quantity 42
    :price 150.00}
   {:id 2
    :name "Blueberry"
    :form-factor :BUSH
    :emoji "🫐"
    :quantity 32
    :price 22.50}
   {:id 3
    :name "Bok Choy"
    :form-factor :SEED
    :emoji "🥬"
    :quantity 64
    :price 1.20}
   {:id 4
    :name "Carrots"
    :form-factor :SEED
    :emoji "🥕"
    :quantity 32
    :price 1.20}
   {:id 5
    :name "Cherry"
    :form-factor :TREE
    :emoji "🍒"
    :quantity 4
    :price 160.00}])

(defn splash []
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "<h1>Hello World!!!</h1>"})

(defroutes app
  (GET "/" []
       (splash))
  (GET "/plant" []
       {:status 200
        :headers {"Content-Type" "application/json"}
        :body (json/generate-string plants)})
  (GET "/plant/:id" [id]
       (let [id-parsed (some->> id (re-find #"\d+") Long/parseLong)
             plant (->> plants (filter #(= (:id %) id-parsed)) first)]
         (if plant
           {:status 200
            :headers {"Content-Type" "application/json"}
            :body (json/generate-string plant)}
           (route/not-found (slurp (io/resource "404.html"))))))
  (ANY "*" []
       (route/not-found (slurp (io/resource "404.html")))))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))]
    (jetty/run-jetty (site #'app) {:port port :join? false})))
