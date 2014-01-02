# 08. Working with Mathematica and R

Clojure가 어떻게 "Mathmetica와 R과 연동"하는가에 대해 설명함.


### [Mathematica](http://www.wolfram.com/mathematica/)란?
- http://mirror.enha.kr/wiki/매스매티카
- 개발자인 스티븐 울프램은 신동이다. 영국 상류층에서 태어나서 이튼 칼리지를 다니다가 자기 수준에 안 맞는다고 생각해서 자퇴하고, 옥스포드 대학에 들어갔는데 강의가 수준이 낮다고 생각하고 2년도 못 버티고 또 자퇴하고 칼텍으로 건너가서 2년이 채 안 지난 20살때 박사학위를 받는다.
- 입자물리를 연구하면서 연구할때 쓸 컴퓨터 프로그램을 짜다가 물리학 때려치우고 이길로 나섰다. 그래서 자기가 짠 컴퓨터 프로그램을 팔기 시작했는데 그것이 바로 매스매티카.


### [R](http://www.r-project.org/)이란?
- R은 통계 계산 및 그래픽을 위한 언어이자 free 소프트웨어 환경.
- KRUG 한국R사용자모임: http://r-project.kr/
- 2013.11 세미나: http://r-project.kr/content/r-manuals-11%EC%9B%94-%EC%84%B8%EB%AF%B8%EB%82%98-%EC%9D%BC%EC%A0%95%EC%9E%85%EB%8B%88%EB%8B%A4


### [Rserve](http://rforge.net/Rserve/)
- Rserve는 다른 프로그램이 R의 기능을 활용할 수 있도록 도와주는 TCP/IP서버.
- Java에서 클라이언트/서버개념 없이 R의 활용하려면, JRI라는 것도 있음. - http://rforge.net/JRI/


### LocalRepo
- https://github.com/kumarshantanu/lein-localrepo
- leiningen의 의존성을 해결하는데 있어, jar파일가지고, 로컬에 저장소를 만드는것.
- 원레는 maven이 해줘야 하는 일이지만.. 간편하게 `lein localrepo install foo-1.0.6.jar com.example/foo 1.0.6`


### 8장에서 쓰는 예제 데이터
- http://www.ericrochester.com/clj-data-analysis/data/all_160.P3.csv



## Setting up Mathematica to talk to Clojuratica for Linux
참고: http://drcabana.org/2012/10/23/installation-and-configuration-of-clojuratica/

환경: Ubuntu 12.04 LTS - 64-bit

* mathmatica 다운로드.
 - http://www.wolfram.com/mathematica/trial/
 - 가입필(email인증)
 - 1.75GB
 - 7시간동안 다운받아야함.


```cmd
$ sudo sh Mathematica_9.0.1_LINUX.sh

Enter the installation directory, or press ENTER to select /usr/local/Wolfram/Mathematica/9.0:
>

Type the directory path in which the Wolfram Mathematica script(s) will be created, or press ENTER to select /usr/local/bin
>

$ sudo mathematica


~$ mkdir ~/clj-interop && cd ~/clj-interop
clj-interop$ wget http://mirror.apache-kr.org/ant/binaries/apache-ant-1.9.2-bin.zip
clj-interop$ unzip apache-ant-1.9.2-bin.zip
clj-interop$ wget https://github.com/stuarthalloway/Clojuratica/archive/master.zip
clj-interop$ unzip master.zip

clj-interop$ ls
apache-ant-1.9.2-bin.zip master.zip
apache-ant-1.9.2  Clojuratica-master

clj-interop$ cd Clojuratica-master
Clojuratica-master$ ../apache-ant-1.9.2/bin/ant
Clojuratica-master$ sudo cp src/mma/*.m /usr/local/Wolfram/Mathematica/9.0/SystemFiles/Autoload/

$ lein localrepo install ~/clj-interop/Clojuratica-master/clojuratica.jar local.repo/clojuratica 2.0.0
$ lein localrepo install /usr/local/Wolfram/Mathematica/9.0/SystemFiles/Links/JLink/JLink.jar local.repo/JLink 9.0.0


$ vi ~/.lein/profiles.clj:

    {:user {:plugins [[lein-localrepo "0.5.2"]]}}

$ export LD_LIBRARY_PATH=/usr/local/Wolfram/Mathematica/9.0/SystemFiles/Links/JLink/SystemFiles/Libraries/Linux-x86-64
```


## Setting up Mathematica to talk to Clojuratica for Windows

* mathmatica 다운로드.
 - http://www.wolfram.com/mathematica/trial/
 - 가입필(email인증)
 - 1.3GB
 - 6시간동안 다운받아야함.
 - 체험판인 경우 1PC에서만 activate됨. (하나의 activate key로, Windows, Linux 동시에 할 수 없음.)

* Ant 다운로드
 - http://ant.apache.org/bindownload.cgi
 - http://mirror.apache-kr.org/ant/binaries/apache-ant-1.9.2-bin.zip

* JDK 다운로드
 - http://www.oracle.com/technetwork/java/javase/downloads/index.html
 - http://download.oracle.com/otn-pub/java/jdk/7u45-b18/jdk-7u45-windows-x64.exe

* Clojuratica 다운로드
 - https://github.com/stuarthalloway/Clojuratica/archive/master.zip


 
```cmd

C:\clj-interop>ls
apache-ant-1.9.2  Clojuratica-master

C:\clj-interop\Clojuratica-master>..\apache-ant-1.9.2\bin\ant.bat
C:\clj-interop\Clojuratica-master>cp src/mma/*.m "C:\Program Files\Wolfram Research\Mathematica\9.0\SystemFiles\Autoload\"

C:\clj-interop>notepad C:\Users\<USERNAME>\.lein\profiles.clj

    {:user {:plugins [[lein-localrepo "0.5.2"]]}}

C:\clj-interop> lein localrepo install C:\clj-interop\Clojuratica-master\clojuratica.jar local.repo/clojuratica 2.0.0
C:\clj-interop> lein localrepo install "C:\Program Files\Wolfram Research\Mathematica\9.0\SystemFiles\Links\JLink\JLink.jar" local.repo/JLink 9.0.0
```



### ERROR
java.lang.UnsatisfiedLinkError: com.wolfram.jlink.NativeLink.MLOpenString(Ljava/lang/String;[Ljava/lang/String;)

 - http://forums.wolfram.com/mathgroup/archive/2008/Aug/msg00664.html

JLINK_LIB_DIR 를 설정하라고 하는데, 그냥 JLinkNativeLibrary.dll을 project폴더에 복사하고 실행시키는 방향으로 감.

JLinkNativeLibrary.dll위치

	C:\Program Files\Wolfram Research\Mathematica\9.0\SystemFiles\Links\JLink\SystemFiles\Libraries\Windows\
	C:\Program Files\Wolfram Research\Mathematica\9.0\SystemFiles\Links\JLink\SystemFiles\Libraries\Windows-x86-64



## Calling Mathematica functions from Clojuratica

lein new mm

project.clj

```clj
(defproject mm "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [local.repo/JLink "9.0.0"]
                 [local.repo/clojuratica "2.0.0"]
                 [incanter "1.5.4"]])
```


src/mm/core.clj

```clj
(ns mm.core)

(use 'clojuratica)
(import [com.wolfram.jlink MathLinkFactory])

;; J/Link
;; Mathematica에서 Java 메소드를 호출 할 수 있음.
;; Mathematica 서비스를 할용가능한  Java프로램을 작성할 수 있음.

(defn init-mma [mma-command]
  (defonce math-evaluate
    (math-evaluator
     (doto
         (MathLinkFactory/createKernelLink mma-command)
       (.discardAnswer)))))


(init-mma
 (str "-linkmode launch -linkname "
     "/usr/local/Wolfram/Mathematica/9.0/Executables/MathKernel"))

(def-math-macro math math-evaluate)
```


src/mm/clojuratica.clj

```clj
(ns mm.clojuratica
  (:use mm.core)
  (:require [incanter.core :as i]
            incanter.stats
            incanter.io))


;; FindRoot
;; 근 찾기
;; http://reference.wolfram.com/mathematica/ref/FindRoot.html

(math (FindRoot [(== (Exp (- x 2)) y) (== (Power y 2) x)]
                [[x 1] [y 1]]))
;=> [(-> x 0.019026016103714054) (-> y 0.13793482556524314)]

(def data-file "data/all_160.P3.csv")

(def data (incanter.io/read-dataset data-file :header true))

(defn mma-mean
  ([dataset col]
     (math (Mean ~(i/sel dataset :cols col)))))

(defn mma-median
  ([dataset col]
     (math (Median ~(i/sel dataset :cols col)))))

(mma-mean data :POP100)
;=> 230766493/29439

(mma-median data :POP100)
;=> 1081

(incanter.stats/mean [1 2 3 4 5])
;=> 3.0

(incanter.stats/median [1 2 3 4 5])
;=> 3.0

(incanter.stats/median [1 2 10])
;=> 2.0

(incanter.stats/median [1 10 90 200])
;=> 50.0


(math (Get "mma/line-integral.m"))
;=> (* 2 Pi)

;; FactorInteger
;; 소인수분해
;; http://reference.wolfram.com/mathematica/ref/FactorInteger.html
(math (FactorInteger 1234567890))
;=> [[2 1] [3 2] [5 1] [3607 1] [3803 1]]



;; ======================================
;; Mathematica Function

(def factor-int
  (math
   (Function [x] (FactorInteger x))))

(factor-int 1234567890)
;=> [[2 1] [3 2] [5 1] [3607 1] [3803 1]]


;; ======================================
;; Parallel
(math (LaunchKernels))

(math (ParallelMap #'(Plus % 1) [1 2 3 4 5]))
;=> [2 3 4 5 6]
;=> [2 3 4 5 6]


(let [f (math :parallel (Function [x] (Fibonacci x)
                                  $KernelID))
      agents (map (fn [_] (agent nil)) (range 10))]
  (dorun (map #(send-off % f) agents))
  (dorun (map await agents))
  (map deref agents))
;=> (4 3 2 1 4 4 4 3 2 3)
;=> (4 2 4 4 1 3 2 4 3 4)
;=> (3 4 1 4 2 2 1 3 4 3)
```


mma/line-integral.m

```m
SyntaxInformation[lineIntegrate] =
    {"LocalVariables" -> {"Plot", {3, 3}},
     "ArgumentsPattern" -> {_, _, _}};

lineIntegrate[r_?VectorQ, f_Function, {t_, tMin_, tMax_}] :=
	Module[{param, localR}, localR = r /. t -> param;
           Integrate[(f[localR, #] Sqrt[#.#]) &@D[localR, param], {param, tMin, tMax}]]

lineIntegrate[{Cos[t], Sin[t]}, 1 &, {t, 0, 2 Pi}]
```


## Setting up R to talk to Clojure
R은 무료 통계 프로그램.


http://craig-russell.co.uk/2012/05/08/install-r-on-ubuntu.html

### Ubuntu 12.04 LTS

```cmd
$ sudo vi /etc/apt/sources/list

	deb http://cran.ma.imperial.ac.uk/bin/linux/ubuntu precise/

$ sudo apt-get update
$ sudo apt-get install r-base

$ R
> install.packages("Rserve")
~/R/x86_64-pc-linux-gnu-library/3.0

$ cd ~/clj-interop
clj-interop$ mkdir Rserve && cd Rserve

Rserve$ wget http://www.rforge.net/Rserve/files/REngine.jar
Rserve$ wget http://www.rforge.net/Rserve/files/RserveEngine.jar
Rserve$ lein localrepo install ~/clj-interop/Rserve/REngine.jar local.repo/REngine 1.8.0
Rserve$ lein localrepo install ~/clj-interop/Rserve/RserveEngine.jar local.repo/RserveEngine 1.8.0

$ export R_HOME=/usr/lib/R
$ export PATH=$PATH:~/R/x86_64-pc-linux-gnu-library/3.0/Rserve/libs
$ Rserve
```


### Windows

* R 다운로드: http://cran.nexr.com/bin/windows/base/
* Rserve 다운로드: http://www.rforge.net/Rserve/files/


```cmd
C:\clj-interop>R.exe
> install.packages("Rserve")

C:\clj-interop\Rserve> wget http://www.rforge.net/Rserve/files/REngine.jar
C:\clj-interop\Rserve> wget http://www.rforge.net/Rserve/files/RserveEngine.jar


lein localrepo install lein localrepo install C:\clj-interop\Rserve\REngine.jar local.repo/REngine 1.8.0
lein localrepo install lein localrepo install C:\clj-interop\Rserve\RserveEngine.jar local.repo/RserveEngine 1.8.0


https://rforge.net/bin/windows/contrib/3.0/Rserve_1.8-0.zip을 다운받고,
압축풀어, Rserve\libs\x64\에 있는 Rserve.*파일들을
R이 설치된 폴더(C:\Program Files\R\R-3.0.2\bin\x64\)에 복사시켜준다.
Rserve.exe 실행
```


## Calling R functions from Clojure

project.clj

```clj
(defproject rr "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [local.repo/REngine "1.8.0"]
                 [local.repo/RserveEngine "1.8.0"]])
```


src/rr/core.clj

```clj
(ns rr.core)

;; ===================
;; Rserve를 이용. R과 연결.

(import '[org.rosuda.REngine REngine]
        '[org.rosuda.REngine.Rserve RConnection])

(def ^:dynamic *r-cxn* (RConnection.))


;; 연결 되었는지 테스트.
(map #(.asDouble %)
     (.. *r-cxn* (eval "qr(c(1,2,3,4,5,6,7))") asList))

;; > qr(c(1,2,3,4,5,6,7))
;; $qr
;;             [,1]
;; [1,] -11.8321596
;; [2,]   0.1690309
;; [3,]   0.2535463
;; [4,]   0.3380617
;; [5,]   0.4225771
;; [6,]   0.5070926
;; [7,]   0.5916080
;; 
;; $rank
;; [1] 1
;; 
;; $qraux
;; qraux is a REAL vector of length P and Qraux is a REAL n by p matrix, as computed by Linpack subroutine dqrdc.
;; [1] 1.084515
;; 
;; $pivot
;; Pivot is a vector of length p containing the column numbers in x corresponding to the successive columns of q and r.
;; [1] 1
;; 
;; attr(,"class")
;; [1] "qr"

;=> (-11.832159566199232 1.0 1.0845154254728517 1.0)


(map #(.asDouble %)
     (.. *r-cxn* (eval "qr(c(-3,-9,-6,-39,-2,8))") asList))
;=> (41.41255848169731 1.0 1.0724417932624444 1.0)

;; ===================
;; Vector를 R로 넘겨주기.

(require '[clojure.string :as str])

(defprotocol ToR
  (->r [x] "Convert an item to R."))

(extend-protocol ToR
  clojure.lang.ISeq
  (->r [coll] (str "c(" (str/join \, (map ->r coll)) ")"))

  clojure.lang.PersistentVector
  (->r [coll] (->r (seq coll)))

  java.lang.Integer
  (->r [i] (str i))
  java.lang.Long
  (->r [l] (str l))
  java.lang.Float
  (->r [f] (str f))
  java.lang.Double
  (->r [d] (str d)))

(defn r-mean
  ([coll] (r-mean coll *r-cxn*))
  ([coll r-cxn]
     (.. r-cxn
         (eval (str "mean(" (->r coll) ")"))
         asDouble)))

(->r [1.0 2.0 3.0])
;=> "c(1.0,2.0,3.0)"

(r-mean [1.0 2.0 3.0])
;=> 2.0

(r-mean (map (fn [_] (rand))
             (range 5)))
;=> 0.46599524431898004


;; ===================
;; R파일 읽어오기.

(import '[java.io File])

;; 카이자승 검증
;; 카이제곱 검정

(defn win-to-xnix-path [winpath]
  (str/replace winpath #"\\" "/"))

(defn r-source
  ([filename] (r-source filename *r-cxn*))
  ([filename r-cxn]
     (.eval r-cxn
             (str "source(\""
                  (win-to-xnix-path (.getAbsolutePath (File. filename)))
                  "\")"))))
  
(def x-sqr (.asList (r-source "r/chisqr-example.R")))

(.. x-sqr (at 0) asList (at "statistic") asDouble)
;=> 1.1581311830842826

(.. x-sqr (at 0) asList (at "parameter") asInteger)
;=> 2

(.. x-sqr (at 0) asList (at "p.value") asDouble)
;=> 0.5604217848398094

(.. x-sqr (at 0) asList names)
;=> ["statistic" "parameter" "p.value" "method" "data.name" "observed" "expected" "residuals" "stdres"]



;; ===================
;; Plotting

(defn r-plot
  ([data filename] (r-plot data filename *r-cxn*))
  ([data filename r-cxn]
     (.. r-cxn
         (eval (str "png(filename=\""
                    (win-to-xnix-path
                     (.getAbsolutePath (File. filename)))
                    "\", height=300, width=250, bg=\"white\")\n"
                    "plot(" (->r data) ")\n"
                    "dev.off()\n")))))

(r-plot [1.0 1.0 2.0 3.0 5.0 8.0 11.0] "fib.png")
;; png(filename="fib.png", height=300, width=250, bg="white")
;; plot(c(1,1,2,3,5,8,11))
;; dev.off()
```


r/chisqr-example.R

```r
dat <- data.frame(q1=sample(c("A","B","C"),size=1000,replace=TRUE),
                  sex=sample(c("M","F"),size=1000,replace=TRUE))
# > dat
#      q1 sex
# 1     C   F
# 2     A   F
# 3     A   F
# 4     B   F
# 5     B   F
# 6     B   M
# ...

dtab <- with(dat,table(q1,sex))
# > dtab
#    sex
# q1    F   M
#   A 149 180
#   B 169 180
#   C 154 168

(Xsq <- chisq.test(dtab))
# > (Xsq <- chisq.test(dtab))
#
#         Pearson's Chi-squared test
#
# data:  dtab
# X-squared = 0.7427, df = 2, p-value = 0.6898
```

## QR분해

## Chi-square Test
sigma{(실제빈도-기대빈도)^2 / 기대빈도}

## 기타
* programming-in-r: http://manuals.bioinformatics.ucr.edu/home/programming-in-r
* rexamples: http://www.rexamples.com/
* J/Link without Java: http://www.youtube.com/watch?v=g9KdQbq_JLE
