
 # 标准表达式

## 表达式

**变量表达式 `"${　　}"`**
```html
<span th:text="${book.author.name}">
```
**消息表达式 `"#{　　}" `**
```html
<span th:text="#{book.author.name}">
```
(文本外部化、国际化、i18n)

**选择表达式 `"*{　　}"`**
```html
<div th:text="${book}">
    <span th:text="*{name}"></span>
</div>
```
**链接表达式 `"@{　　}"`**
```html
<a th:href="@{../doucments/report}">...</a>
<a th:href="@{~/doucments/report}">...</a>
<a th:href="@{//static.mycompany.com/report}">...</a>
<a th:href="@{http://www.mycompany.com/doucments/report}">...</a>
```
**分段表达式 `th:insert th:replace`**
```html
<!doctype html>
<html xmls:th="http://www.thymeleaf.org">
    <body>
        <div th:fragment="copy">
            &copy; 2017 <a href="https//waylau.com">waylau.com</a>
    </body>
</html>
<body>
    ...
    <div th:insert="~{foot :: copy }"></div>
</body>
```

## 字面量

**文本**
```html
<p>
    Now you are looking at a <span th:text="'working web application'">template file</span>.
</p>
```
**数字**
```html
<p>
    The year is <span th:text="2013">1492</span>.
</p>
```
**布尔**
`true、false`

**空** `null`

**算术操作** `+、-、*、／、%`

**比较** `>、<、>=、<=（gt、lt、ge、le）`

**等价** `==、!= (eq ne)`

**三目运算符** `？ ：`

**无操作** `_`
