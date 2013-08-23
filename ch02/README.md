2. Cleaning and Validating Data
===========================

## Identifying and removing duplicate data 45
clj-diff
https://github.com/brentonashworth/clj-diff

clojure.data/diff
http://clojuredocs.org/clojure_core/clojure.data/diff

## Rescaling values 50
* TF-IDF(Term Frequency - Inverse Document Frequency)�� ���� �˻��� �ؽ�Ʈ ���̴׿��� �̿��ϴ� ����ġ��, ���� ������ �̷���� �������� ���� �� � �ܾ Ư�� ���� ������ �󸶳� �߿��� �������� ��Ÿ���� ����� ��ġ�̴�. ������ �ٽɾ �����ϰų�, �˻� �������� �˻� ����� ������ �����ϰų�, ������ ������ ����� ������ ���ϴ� ���� �뵵�� ����� �� �ִ�. - http://ko.wikipedia.org/wiki/TF-IDF

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
