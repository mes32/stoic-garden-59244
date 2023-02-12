(ns stoic-garden-59244.core
  (:require [cheshire.core :as json]
            [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [ring.adapter.jetty :as jetty]
            [environ.core :refer [env]]))

(def plant-form-factors #{:BUSH :SEED :TREE})

;; TODO - move this inside plants object
(def last-plant-id (atom 5))

(def plants (atom
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
    :price 160.00}]))

(defn- get-plant [id-string]
  (let [id (some->> id-string (re-find #"\d+") Long/parseLong)
        plant (->> @plants (filter #(= (:id %) id)) first)]
    plant))

(defn splash []
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (str "<h1>Hello World!</h1>"
              "<p>--stoic-garden-59244</p>")})

(defroutes app
  (GET "/" []
       (splash))
  (GET "/plant" []
       {:status 200
        :headers {"Content-Type" "application/json"}
        :body (json/generate-string @plants)})
  (GET "/plant/:id" [id]
       (let [plant (get-plant id)]
         (if plant
           {:status 200
            :headers {"Content-Type" "application/json"}
            :body (json/generate-string plant)}
           (route/not-found (slurp (io/resource "404.html"))))))
  (DELETE "/plant/:id" [id]
          (let [id-long (some->> id (re-find #"\d+") Long/parseLong)]
            (swap! plants (fn [p] (remove #(= (:id %) id-long) p)))
            {:status 204
             :headers {"Content-Type" "application/json"}}))
  (ANY "*" []
       (route/not-found (slurp (io/resource "404.html")))))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))]
    (jetty/run-jetty (site #'app) {:port port :join? false})))
