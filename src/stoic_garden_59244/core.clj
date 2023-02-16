(ns stoic-garden-59244.core
  (:require [cheshire.core :as json]
            [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [ring.adapter.jetty :as jetty]
            [environ.core :refer [env]]))

(def plant-form-factors #{:BUSH :SEED :TREE})

(defn- parse-double [input-string]
  (some->> input-string (re-find #"\d+") Double/parseDouble))

(defn- parse-form-factor [input-string]
  (some-> input-string clojure.string/upper-case keyword plant-form-factors))

(defn- parse-int [input-string]
  (some->> input-string (re-find #"\d+") Integer/parseInt))

(defn- parse-long [input-string]
  (some->> input-string (re-find #"\d+") Long/parseLong))

(def plants (atom
  {:last-id 5
   :list [{:id 1
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
           :price 160.00}]}))

(defn- new-plant [raw-request-string]
  (let [plant-params (json/parse-string raw-request-string true)]
    {:name (:name plant-params)
     :form-factor (-> plant-params :form-factor parse-form-factor)
     :emoji (:emoji plant-params)
     :quantity (-> plant-params :quantity parse-int)
     :price (-> plant-params :price parse-double)}))

(defn- valid-plant? [plant]
  (and (get plant :name)
       (get plant :form-factor)
       (get plant :emoji)
       (get plant :quantity)
       (get plant :price)))

(defn- insert-plant! [plant]
  (swap! plants
         (fn [plants-map]
           (let [new-id (inc (:last-id plants-map))]
             (-> plants-map
                 (assoc :list (conj (:list plants-map) (assoc plant :id new-id)))
                 (assoc :last-id new-id))))))

(defn- get-plant [id-string]
  (->> @plants 
       :list 
       (filter #(= (:id %) (parse-long id-string)))
       first))

(defn splash []
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (str "<h1>Hello World!</h1>"
              "<p>--stoic-garden-59244</p>")})

(defroutes app
  (GET "/" []
       (splash))
  (POST "/plant" {body :body}
        (let [plant (new-plant (slurp body))]
          (if (valid-plant? plant)
            (do
              (insert-plant! plant)
              {:status 201
               :headers {"Content-Type" "application/json"}})
            {:status 422
             :headers {"Content-Type" "application/json"}
             :body (json/generate-string {:message "Invalid plant params."})})))
  (GET "/plant" []
       {:status 200
        :headers {"Content-Type" "application/json"}
        :body (-> @plants :list json/generate-string)})
  (GET "/plant/:id" [id]
       (let [plant (get-plant id)]
         (if plant
           {:status 200
            :headers {"Content-Type" "application/json"}
            :body (json/generate-string plant)}
           (route/not-found (slurp (io/resource "404.html"))))))
  (DELETE "/plant/:id" [id]
          (do
            (swap! plants
                   (fn [plants-map]
                     (assoc plants-map :list (remove #(= (:id %) (parse-long id)) (:list plants-map)))))
            {:status 204
             :headers {"Content-Type" "application/json"}}))
  (ANY "*" []
       (route/not-found (slurp (io/resource "404.html")))))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 5000))]
    (jetty/run-jetty (site #'app) {:port port :join? false})))
