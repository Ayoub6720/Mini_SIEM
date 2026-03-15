# Analyseur de logs securite - Mini-SIEM (Java)

Mini-SIEM en ligne de commande qui analyse des logs, detecte les tentatives de brute-force, produit des statistiques par IP et genere des rapports TXT/CSV/JSON.

## Fonctionnalites
- Parsing robuste des logs (lignes invalides ignorees)
- Statistiques par IP (total, succes, echecs, types d'evenements, premieres/dernieres apparitions)
- Detection de brute-force (seuil + fenetre temporelle configurables)
- Rapports TXT/CSV/JSON
- Resume console
- Analyse d'un fichier ou d'un dossier de logs

## Structure
- `src/main/java/com/mini_siem` : code source Java
- `samples/sample.log` : exemple de logs
- `reports/` : dossier de sortie par defaut

## Format de logs supporte
```
[YYYY-MM-DD HH:MM:SS] IP=1.2.3.4 EVENT=LOGIN STATUS=FAIL MESSAGE=Invalid password
```

## Execution
```bat
dir /s /b src\main\java\*.java > sources.txt
javac -d out @sources.txt
java -cp out com.mini_siem.Main -i samples\sample.log -o reports -t 5 -w 10
```

Ou simplement:
```bat
run.bat
```

Note: `sources.txt` est genere localement pour la compilation et est ignore par Git.

Pour choisir le fichier/dossier a analyser:
```bat
run.bat samples\sample.log
```
Si aucun argument n'est fourni, `run.bat` demande le chemin interactif.

Pour parametrer seuil et fenetre:
```bat
run.bat samples\sample.log 5 10
```

## Parametres
- `-i, --input` : fichier log ou dossier contenant des logs
- `-o, --out` : dossier de sortie des rapports (defaut: `reports`)
- `-t, --threshold` : seuil brute-force (defaut: 5)
- `-w, --window` : fenetre temporelle en minutes (defaut: 10)
- `-h, --help` : afficher l'aide

## Exemple rapide
```bat
java -cp out com.mini_siem.Main -i samples\sample.log -o reports
```

Les rapports generes:
- `reports/report.txt`
- `reports/report.csv`
- `reports/report.json`
