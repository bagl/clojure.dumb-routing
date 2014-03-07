(ns dumb-routing.core-test
  (:require [clojure.test :refer :all]
            [dumb-routing.core :refer [basic-handler]]))

(defn request [method uri]
  {:server-port 80
   :server-name "localhost"
   :remote-addr "localhost"
   :query-string nil
   :scheme :http
   :headers  {"host" "localhost"} 
   :uri uri
   :request-method method})

(def routes
  [[#"/api"
    (fn [_] "api")]

   [#"/api/first/(.+)/second/(.+)"
    #(fn [_] (str "first = " %1 " --- second = " %2))]

   [#"/api/first/(.+)"
    #(fn [_] (str "api first = " %))]

   [#"/api/(.+)"
    #(fn [_] (str "api - " %))]])

(deftest test-basic-handler

  (is (= {:status 404 :headers {} :body "not found"}
         ((basic-handler routes)
          (request :get "/nonexistent"))))
  
  (is (= "nothing here"
         ((basic-handler routes (fn [_] "nothing here"))
          (request :get "/nonexistent"))))

  (is (= "api"
         ((basic-handler routes)
          (request :get "/api"))))

  (is (= "api"
         ((basic-handler routes)
          (request :post "/api"))))
  
  (is (= "first = v1 --- second = v2"
         ((basic-handler routes)
          (request :get "/api/first/v1/second/v2")))))
