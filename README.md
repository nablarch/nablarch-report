# nablarch-report

**本機能は後述している依存ライブラリが現在入手困難となっているため、今後メンテナンスは行われません。**

**帳票出力機能が必要な場合は、以下のような代替手段を検討してください。**

* **商用製品を利用する**
* **ブラウザやOfficeのPDF出力機能を利用する**

| master | develop |
|:-----------|:------------|
|[![Build Status](https://travis-ci.org/nablarch/nablarch-report.svg?branch=master)](https://travis-ci.org/nablarch/nablarch-report)|[![Build Status](https://travis-ci.org/nablarch/nablarch-report.svg?branch=develop)](https://travis-ci.org/nablarch/nablarch-report)|

## 依存ライブラリ

本モジュールのコンパイルまたはテストには、下記ライブラリを手動でローカルリポジトリへインストールする必要があります。

ライブラリ          |ファイル名            |グループID     |アーティファクトID   |バージョン   |
:-------------------|:---------------------|:--------------|:--------------------|:------------|
iTextAsian          |iTextAsian-1.0.0.jar  |com.lowagie    |iTextAsian           |1.0.0        |
※iTextAsianは、jarファイルを本リポジトリの/lib配下に格納してあります。ライセンスに関しては、jarファイル内のcmap_info.txtを参照してください。

上記ライブラリは、下記コマンドでインストールしてください。


```
mvn install:install-file -Dfile=<ファイル名> -DgroupId=<グループID> -DartifactId=<アーティファクトID> -Dversion=<バージョン> -Dpackaging=jar
```
