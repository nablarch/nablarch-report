# nablarch-report

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
