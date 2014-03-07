(ns dumb-routing.util-test
  (:require [clojure.test :refer :all]
            [dumb-routing.util :as util]))

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

(def routes
  [[#"/api"
    (fn [_] "api")]

   [#"/api/first/(.+)/second/(.+)"
    #(fn [_] (str "first = " %1 " --- second = " %2))]

   [#"/api/first/(.+)"
    #(fn [_] (str "api first = " %))]

   [#"/api/(.+)"
    #(fn [_] (str "api - " %))]])

(deftest test-find-route

  (is (= nil (util/find-route routes "/nonexistent")))

  (are [n uri] (= (nth routes n)
                      (util/find-route routes uri))
       0 "/api"
       1 "/api/first/v1/second/v2"
       2 "/api/first/v1"
       3 "/api/v1"))

(deftest test-build-handler

  (is (= ((second (nth routes 0)) {})
         ((util/build-handler (nth routes 0) "/api") {})))

  (are [n uri pars] (= ((apply (second (nth routes n)) pars) {})
                       ((util/build-handler (nth routes n) uri) {}))

       1 "/api/first/v1/second/v2" ["v1" "v2"]
       2 "/api/first/v1" ["v1"]
       3 "/api/other" ["other"]))
