# dumb-routing

A Clojure library for routing Ring requests

## Installation

Add the following dependency to `project.clj`:

```clojure
[dumb-routing "0.0.1"]
```

## Usage

```clojure
(ns my.awesome.web.app
  (:require [dumb-routing.core :refer [basic-handler]]
            [org.httpkit.server :refer [run-server]]))

(defn response [body]
  {:status 200 :headers {} :body body})

(def routes
  [[#"/no-params"
    (fn [_] (response "no params"))]

   [#"/one-param/(.+)"
    (fn [par]
      (fn [_] (response (str "param = " par))))]

   [#"/two-params/([^/]+)/(.+)"
    (fn [par1 par2]
      (fn [_] (response (str "param 1 = " par1 " and param 2 = " par2))))]

   [#"/only-numerics/(\d+)"
    (fn [number-like-par]
      (fn [_] (response (str "numeric param = " number-like-par))))]])

(def not-found
  (fn [_] {:status 404 :headers {} :body "nothing here"}))

(def app
  (basic-handler routes not-found))

(def server
  (run-server #'app {:port 8080}))
```

`basic-handler` takes a vector of routes and optionally a default handler that
will be called when no route matches.

Route is a vector of `[uri-regexp to-be-handler]`. When uri-regexp matches any
groups, it calls the `to-be-handler` with the matched values and expect
it to return the Ring handler itself. If no groups are matched, it returns
`to-be-handler`.

Thus, if you need to pass parameters from uri to your handler, `to-be-handler`
should be a function of as many arguments as groups in the uri and should
return the appropriate Ring handler. If there are no groups, the route should
contain the handler itself.

The first route that matches wins.
