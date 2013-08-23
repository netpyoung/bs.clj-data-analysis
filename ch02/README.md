2. Cleaning and Validating Data
===========================

## Identifying and removing duplicate data 45
clj-diff
https://github.com/brentonashworth/clj-diff

clojure.data/diff
http://clojuredocs.org/clojure_core/clojure.data/diff

## Rescaling values 50
* TF-IDF(Term Frequency - Inverse Document Frequency)는 정보 검색과 텍스트 마이닝에서 이용하는 가중치로, 여러 문서로 이루어진 문서군이 있을 때 어떤 단어가 특정 문서 내에서 얼마나 중요한 것인지를 나타내는 통계적 수치이다. 문서의 핵심어를 추출하거나, 검색 엔진에서 검색 결과의 순위를 결정하거나, 문서들 사이의 비슷한 정도를 구하는 등의 용도로 사용할 수 있다. - http://ko.wikipedia.org/wiki/TF-IDF

## Normalizing dates and times 51
http://joda-time.sourceforge.net/
https://github.com/clj-time/clj-time

## Sampling from very large data sets 56


## Fixing spelling errors 57
How to Write a Spelling Corrector
http://norvig.com/spell-correct.html

## Parsing custom data formats 61
http://en.wikipedia.org/wiki/FASTA_format
https://github.com/protoflex/parse-ez

## Validating data with Valip 64
valip
https://github.com/weavejester/valip
Valip is a validation library for Clojure. It is primarily designed to validate keyword-string maps, such as one might get from a HTML form.


com.cemerick/valip
https://github.com/cemerick/valip
1. I'm trying to make this fork as portable as possible between Clojure and (JavaScript) ClojureScript.
2. Since I'm breaking stuff anyway, I'm making all sorts of changes and additions to the original predicates and such, hopefully all for the better.

A validation DSL for Clojure apps
https://github.com/leonardoborges/bouncer
