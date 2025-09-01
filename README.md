# Zara E2E Automation

Java tabanlı **UI + API uçtan uca test otomasyonu**  
Teknolojiler: **Java (POM’daki sürüm), Maven, Cucumber, JUnit4, Selenium, Rest-Assured, Log4j2**

---

## Proje Yapısı
```
reports/
├─ cucumber/
├─ data/
├─ screenshots/
└─ surefire/

src/
├─ main/
│ ├─ java/
│ │ └─ com/company/automation/core/
│ │ ├─ driver/
│ │ └─ utils/ # ApiBase burada
│ └─ resources/
└─ test/
├─ java/
│ └─ com/company/automation/
│ ├─ hooks/
│ ├─ pages/
│ ├─ runners/ # E2ERunnerTest burada
│ ├─ steps/ # TrelloApiSteps burada
│ └─ utils/ # ConfigReader burada
└─ resources/
├─ config/ # test-user.properties
├─ data/
└─ features/ # .feature dosyaları
```


Önemli yollar:
- **ApiBase** → `src/main/java/com/company/automation/core/utils/ApiBase.java`
- **TrelloApiSteps** → `src/test/java/com/company/automation/steps/TrelloApiSteps.java`
- **Runner** → `src/test/java/com/company/automation/runners/E2ERunnerTest.java`
- **ConfigReader** → `src/test/java/com/company/automation/utils/ConfigReader.java`
- **Properties** → `src/test/resources/config/test-user.properties`

---

## Gereksinimler

- **JDK**: POM’daki `<maven.compiler.release>` değerine uygun JDK
- **Maven** yüklü

Kontrol:
```bash

java -version
mvn -v 
```


## Config Dosyası
```
src/test/resources/config/test-user.properties:

user.email=...
user.password=...
user.user=ZARA
trello.key=...
trello.token=...
```

## Koddan erişim:
```
String key = ConfigReader.get("trello.key");
String token = ConfigReader.get("trello.token");
```


## Testleri Çalıştırma
### Tüm testler
```bash

mvn test
```

### Sadece UI
```bash

mvn test -Dcucumber.filter.tags="@ui"
```

### Sadece API (Trello)
```bash

mvn test -Dcucumber.filter.tags="@trello"
```

### Belirli feature
```bash

mvn test -Dcucumber.features=src/test/resources/features/trello_board_cards.feature
```

## Raporlar ve Çıktılar

Cucumber → reports/cucumber/

cucumber.html, cucumber.json, cucumber.xml

Surefire → reports/surefire/

Screenshots → reports/screenshots/

API/Text → reports/data/

## Başlıca Bileşenler

ApiBase: Rest-Assured tabanlı API helper

TrelloApiSteps: Trello board & card senaryoları

ConfigReader: properties yükleyici

E2ERunnerTest: Cucumber runner (glue: steps, hooks)

CucumberHooks: UI senaryolarında WebDriver yönetimi (@ui tag’li senaryolarda çalışır)



## Trello Senaryosu (Örnek)

```
src/test/resources/features/trello_board_cards.feature:

Feature: Trello Board and Cards API
  Scenario: Create board, add cards, update one card, delete cards and board
    When the client creates a new Trello board
    And the client creates two cards on the board
    And the client updates one random card
    And the client deletes both cards
    Then the client deletes the board
```