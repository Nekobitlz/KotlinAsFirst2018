@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson7.task1

import java.io.File

/**
 * Пример
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * Вывести его в выходной файл с именем outputName, выровняв по левому краю,
 * чтобы длина каждой строки не превосходила lineLength.
 * Слова в слишком длинных строках следует переносить на следующую строку.
 * Слишком короткие строки следует дополнять словами из следующей строки.
 * Пустые строки во входном файле обозначают конец абзаца,
 * их следует сохранить и в выходном файле
 */
fun alignFile(inputName: String, lineLength: Int, outputName: String) {
    val outputStream = File(outputName).bufferedWriter()
    var currentLineLength = 0
    for (line in File(inputName).readLines()) {
        if (line.isEmpty()) {
            outputStream.newLine()
            if (currentLineLength > 0) {
                outputStream.newLine()
                currentLineLength = 0
            }
            continue
        }
        for (word in line.split(" ")) {
            if (currentLineLength > 0) {
                if (word.length + currentLineLength >= lineLength) {
                    outputStream.newLine()
                    currentLineLength = 0
                }
                else {
                    outputStream.write(" ")
                    currentLineLength++
                }
            }
            outputStream.write(word)
            currentLineLength += word.length
        }
    }
    outputStream.close()
}

/**
 * Средняя
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * На вход подаётся список строк substrings.
 * Вернуть ассоциативный массив с числом вхождений каждой из строк в текст.
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 *
 */
fun countSubstrings(inputName: String, substrings: List<String>): Map<String, Int> {
    val result = mutableMapOf<String, Int>()
    val text = File(inputName)
            .readLines()
            .joinToString(" ")

    for (i in 0 until substrings.size) {
        result[substrings[i]] = Regex(substrings[i].toLowerCase())
                .findAll(text.toLowerCase())
                .count()
    }

    return result
}


/**
 * Средняя
 *
 * В русском языке, как правило, после букв Ж, Ч, Ш, Щ пишется И, А, У, а не Ы, Я, Ю.
 * Во входном файле с именем inputName содержится некоторый текст на русском языке.
 * Проверить текст во входном файле на соблюдение данного правила и вывести в выходной
 * файл outputName текст с исправленными ошибками.
 *
 * Регистр заменённых букв следует сохранять.
 *
 * Исключения (жюри, брошюра, парашют) в рамках данного задания обрабатывать не нужно
 *
 */
fun sibilants(inputName: String, outputName: String) {
    val text = File(inputName).readLines()
    val result = File(outputName).bufferedWriter()
    val consonants = listOf('Ж', 'Ч', 'Ш', 'Щ', 'ж', 'ч', 'ш', 'щ')
    val notAllowedVowels = listOf('Ы', 'ы', 'Я', 'я', 'Ю', 'ю')
    val allowedVowels = listOf('И', 'и', 'А', 'а', 'У', 'у')

    for (line in text) {
        if (line.length > 1) {
            result.write(line[0].toString())

            // Пробегаемся по всей строке и если встречаем запрещенную букву,
            // перед которой стоит шипящая, то заменяем на разрешенную букву
            // иначе просто записываем в результат
            for (i in 1 until line.length) {
                if (line[i - 1] in consonants && line[i] in notAllowedVowels)
                    result.write(allowedVowels[notAllowedVowels.indexOf(line[i])].toString())
                else
                    result.write(line[i].toString())
            }
        }
        else
            result.write(line)

        result.newLine()
    }

    result.close()
}

/**
 * Средняя
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 * Вывести его в выходной файл с именем outputName, выровняв по центру
 * относительно самой длинной строки.
 *
 * Выравнивание следует производить путём добавления пробелов в начало строки.
 *
 *
 * Следующие правила должны быть выполнены:
 * 1) Пробелы в начале и в конце всех строк не следует сохранять.
 * 2) В случае невозможности выравнивания строго по центру, строка должна быть сдвинута в ЛЕВУЮ сторону
 * 3) Пустые строки не являются особым случаем, их тоже следует выравнивать
 * 4) Число строк в выходном файле должно быть равно числу строк во входном (в т. ч. пустых)
 *
 */
fun centerFile(inputName: String, outputName: String) {
    val text = File(inputName).readLines()
    val result = File(outputName).bufferedWriter()
    var maxLine = -1

    for (line in text) {
        if (line.trim().length > maxLine) {
            maxLine = line.trim().length
        }
    }

    for (line in text) {
        val lineLength = line.trim().length

        // Если эта строка не максимальной длины, то добавляем слева кол-во пробелов,
        // равное половине разницы с максимальной строкой (т.к. если добавлять полную разницу - строка сдивнется вправо)
        if (lineLength != maxLine) {
            val distance = (maxLine - lineLength) / 2

            result.write((" ".repeat(distance)) + line.trim())
        }
        else
            result.write(line)

        result.newLine()
    }

    result.close()
}

/**
 * Сложная
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 * Вывести его в выходной файл с именем outputName, выровняв по левому и правому краю относительно
 * самой длинной строки.
 * Выравнивание производить, вставляя дополнительные пробелы между словами: равномерно по всей строке
 *
 * Слова внутри строки отделяются друг от друга одним или более пробелом.
 *
 * Следующие правила должны быть выполнены:
 * 1) Каждая строка входного и выходного файла не должна начинаться или заканчиваться пробелом.
 * 2) Пустые строки или строки из пробелов трансформируются в пустые строки без пробелов.
 * 3) Строки из одного слова выводятся без пробелов.
 * 4) Число строк в выходном файле должно быть равно числу строк во входном (в т. ч. пустых).
 *
 * Равномерность определяется следующими формальными правилами:
 * 5) Число пробелов между каждыми двумя парами соседних слов не должно отличаться более, чем на 1.
 * 6) Число пробелов между более левой парой соседних слов должно быть больше или равно числу пробелов
 *    между более правой парой соседних слов.
 *
 * Следует учесть, что входной файл может содержать последовательности из нескольких пробелов  между слвоами. Такие
 * последовательности следует учитывать при выравнивании и при необходимости избавляться от лишних пробелов.
 * Из этого следуют следующие правила:
 * 7) В самой длинной строке каждая пара соседних слов должна быть отделена В ТОЧНОСТИ одним пробелом
 * 8) Если входной файл удовлетворяет требованиям 1-7, то он должен быть в точности идентичен выходному файлу
 */
fun alignFileByWidth(inputName: String, outputName: String) {
    val text = File(inputName).readLines()
    val result = File(outputName).bufferedWriter()
    var maxLine = -1

    for (line in text) {
        if (line.trim().length > maxLine) {
            maxLine = line.trim().length
        }
    }

    for (line in text) {
        // Разбиваем строку на слова и кидаем их в список
        // Далее делаем из списка строку и до тех пор, пока она меньше,
        // чем максмальная строка - добавляем в каждое слово пробел
        // *возможно можно сделать без постоянного перевода в строку на каждом шаге, но у меня не получилось(*
        val words = line.split(" ")
                .filter {
                    it != ""
                }
                .toMutableList()

        if (words.size > 1) {
            while (maxLine > words.joinToString("").length)
                for (i in 0 until words.size - 1)
                    if (maxLine > words.joinToString("").length)
                        words[i] += " "
                    else
                        break

            result.write(words.joinToString(""))
        }
        else
            result.write(line.trim())

        result.newLine()
    }

    result.close()
}

/**
 * Средняя
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 *
 * Вернуть ассоциативный массив, содержащий 20 наиболее часто встречающихся слов с их количеством.
 * Если в тексте менее 20 различных слов, вернуть все слова.
 *
 * Словом считается непрерывная последовательность из букв (кириллических,
 * либо латинских, без знаков препинания и цифр).
 * Цифры, пробелы, знаки препинания считаются разделителями слов:
 * Привет, привет42, привет!!! -привет?!
 * ^ В этой строчке слово привет встречается 4 раза.
 *
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 * Ключи в ассоциативном массиве должны быть в нижнем регистре.
 *
 */
fun top20Words(inputName: String): Map<String, Int> {
    val result = mutableMapOf<String, Int>()
    val text = File(inputName)
            .readLines()
            .joinToString(" ")
            .toLowerCase()
            .split(Regex("""\d|\s|\p{P}"""))
            .filter(Regex("""[a-zёа-я+]+""")::matches)

    for (i in 0 until text.size) {
        if (result.containsKey(text[i]))
            result[text[i]] = result[text[i]]!!.plus(1)
        else
            result[text[i]] = 1
    }

    return result
            .toList()
            .sortedByDescending {
                it.second
            }
            .take(20)
            .toMap()
}

/**
 * Средняя
 *
 * Реализовать транслитерацию текста из входного файла в выходной файл посредством динамически задаваемых правил.

 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 *
 * В ассоциативном массиве dictionary содержится словарь, в котором некоторым символам
 * ставится в соответствие строчка из символов, например
 * mapOf('з' to "zz", 'р' to "r", 'д' to "d", 'й' to "y", 'М' to "m", 'и' to "yy", '!' to "!!!")
 *
 * Необходимо вывести в итоговый файл с именем outputName
 * содержимое текста с заменой всех символов из словаря на соответствующие им строки.
 *
 * При этом регистр символов в словаре должен игнорироваться,
 * но при выводе символ в верхнем регистре отображается в строку, начинающуюся с символа в верхнем регистре.
 *
 * Пример.
 * Входной текст: Здравствуй, мир!
 *
 * заменяется на
 *
 * Выходной текст: Zzdrавствуy, mир!!!
 *
 * Пример 2.
 *
 * Входной текст: Здравствуй, мир!
 * Словарь: mapOf('з' to "zZ", 'р' to "r", 'д' to "d", 'й' to "y", 'М' to "m", 'и' to "YY", '!' to "!!!")
 *
 * заменяется на
 *
 * Выходной текст: Zzdrавствуy, mир!!!
 *
 * Обратите внимание: данная функция не имеет возвращаемого значения
 */
fun transliterate(inputName: String, dictionary: Map<Char, String>, outputName: String) {
    TODO()
    /**val text = File(inputName).readLines()
    val result = File(outputName).bufferedWriter()
    val lowerDictionary = dictionary
            .mapValues {
                it.value.toLowerCase()
            }
            .mapKeys{
                it.key.toLowerCase()
            }

    for (line in text) {
        var currentLine = ""

        for (i in 0 until line.length) {
            currentLine += if (lowerDictionary.containsKey(line[i].toLowerCase()))
                (lowerDictionary[line[i].toLowerCase()])
            else
                (line[i].toString())
        }

        currentLine.replace(currentLine[0], currentLine[0].toUpperCase())

        result.write(currentLine)
        result.newLine()
    }

    result.close()*/
}

/**
 * Средняя
 *
 * Во входном файле с именем inputName имеется словарь с одним словом в каждой строчке.
 * Выбрать из данного словаря наиболее длинное слово,
 * в котором все буквы разные, например: Неряшливость, Четырёхдюймовка.
 * Вывести его в выходной файл с именем outputName.
 * Если во входном файле имеется несколько слов с одинаковой длиной, в которых все буквы разные,
 * в выходной файл следует вывести их все через запятую.
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 *
 * Пример входного файла:
 * Карминовый
 * Боязливый
 * Некрасивый
 * Остроумный
 * БелогЛазый
 * ФиолетОвый

 * Соответствующий выходной файл:
 * Карминовый, Некрасивый
 *
 * Обратите внимание: данная функция не имеет возвращаемого значения
 */
fun chooseLongestChaoticWord(inputName: String, outputName: String) {
    val text = File(inputName).readLines()
    val result = File(outputName).bufferedWriter()
    var maxLineLength = -1
    val maxLineList = mutableListOf<String>()

    for (line in text) {
        if (line.length < maxLineLength)
            continue
        else {
            var currentLine = ""
            var wrongLine = false

            for (i in 0 until line.length) {
                if (!currentLine.contains(line[i].toLowerCase()))
                    currentLine += line[i].toLowerCase()
                else {
                    //Если буква встречалась ранее, то прерываем оба цикла и переходим к следующей строке
                    wrongLine = true
                    break
                }
            }

            if (wrongLine)
                break

            //Если вся новая строка состоит из разных букв, то возвращаем изначальную (чтобы сохранить регистр букв)
            currentLine = line

            when {
                currentLine.length > maxLineLength -> {
                    maxLineLength = currentLine.length
                    maxLineList.clear()
                    maxLineList.add(currentLine)
                }
                currentLine.length == maxLineLength ->
                    maxLineList.add(currentLine)
                else -> {/*NOTHING*/}
            }
        }
    }

    result.write(maxLineList.joinToString(", "))
    result.close()
}

/**
 * Сложная
 *
 * Реализовать транслитерацию текста в заданном формате разметки в формат разметки HTML.
 *
 * Во входном файле с именем inputName содержится текст, содержащий в себе элементы текстовой разметки следующих типов:
 * - *текст в курсивном начертании* -- курсив
 * - **текст в полужирном начертании** -- полужирный
 * - ~~зачёркнутый текст~~ -- зачёркивание
 *
 * Следует вывести в выходной файл этот же текст в формате HTML:
 * - <i>текст в курсивном начертании</i>
 * - <b>текст в полужирном начертании</b>
 * - <s>зачёркнутый текст</s>
 *
 * Кроме того, все абзацы исходного текста, отделённые друг от друга пустыми строками, следует обернуть в теги <p>...</p>,
 * а весь текст целиком в теги <html><body>...</body></html>.
 *
 * Все остальные части исходного текста должны остаться неизменными с точностью до наборов пробелов и переносов строк.
 * Отдельно следует заметить, что открывающая последовательность из трёх звёздочек (***) должна трактоваться как "<b><i>"
 * и никак иначе.
 *
 * Пример входного файла:
Lorem ipsum *dolor sit amet*, consectetur **adipiscing** elit.
Vestibulum lobortis, ~~Est vehicula rutrum *suscipit*~~, ipsum ~~lib~~ero *placerat **tortor***,

Suspendisse ~~et elit in enim tempus iaculis~~.
 *
 * Соответствующий выходной файл:
<html>
    <body>
        <p>
            Lorem ipsum <i>dolor sit amet</i>, consectetur <b>adipiscing</b> elit.
            Vestibulum lobortis. <s>Est vehicula rutrum <i>suscipit</i></s>, ipsum <s>lib</s>ero <i>placerat <b>tortor</b></i>.
        </p>
        <p>
            Suspendisse <s>et elit in enim tempus iaculis</s>.
        </p>
    </body>
</html>
 *
 * (Отступы и переносы строк в примере добавлены для наглядности, при решении задачи их реализовывать не обязательно)
 */
fun markdownToHtmlSimple(inputName: String, outputName: String) {
    val text = File(inputName).readLines()
    val result = File(outputName).bufferedWriter()

    result.write("<html><body><p>" + textToHtml(findAllParagraphs(text).joinToString(" ")) + "</p></body></html>")
    result.close()
}

/**
 * Вспомогательная
 * Находит все текстовые разметки в тексте и заменяет их на тэги
 */
fun textToHtml(text: String): String {
    var result = ""
    //Если переменная равна true, значит следующий такой тэг будет закрытым
    var italicHasPair = false
    var boldHasPair = false
    var strikeHasPair = false
    var step = 0

    //Если встречаем последовательность символов - заменяем их на эквивалентные тэги HTML,
    //проверяя есть ли уже незакрытый тэг
    while (step <= text.length - 1) {
        when {
            (text[step] == '*') && (text[step + 1] == '*') -> {
                if (boldHasPair) {
                    result += "</b>"
                    boldHasPair = false
                } else {
                    result += "<b>"
                    boldHasPair = true
                }

                step += 2
            }
            (text[step] == '*') -> {
                if (italicHasPair) {
                    result += "</i>"
                    italicHasPair = false
                } else {
                    result += "<i>"
                    italicHasPair = true
                }

                step++
            }
            (text[step] == '~') && (text[step + 1] == '~') -> {
                if (strikeHasPair) {
                    result += "</s>"
                    strikeHasPair = false
                } else {
                    result += "<s>"
                    strikeHasPair = true
                }

                step += 2
            }
            else -> {
                result += text[step]
                step++
            }
        }
    }

    return result
}

/**
 * Вспомогательная
 * Находит все абзацы в тексте и заменяет их на тэги
 */
fun findAllParagraphs(text: List<String>): List<String> {
    var result = listOf<String>()

    //Если строка пустая - заменяет её на тэги абзаца
    for (line in text) {
        if (text[0].startsWith('\n'))
            continue

        result += if (line.isEmpty())
            "</p><p>"
        else
            line
    }

    return result
}

@Suppress("NAME_SHADOWING")
        /**
 * Сложная
 *
 * Реализовать транслитерацию текста в заданном формате разметки в формат разметки HTML.
 *
 * Во входном файле с именем inputName содержится текст, содержащий в себе набор вложенных друг в друга списков.
 * Списки бывают двух типов: нумерованные и ненумерованные.
 *
 * Каждый элемент ненумерованного списка начинается с новой строки и символа '*', каждый элемент нумерованного списка --
 * с новой строки, числа и точки. Каждый элемент вложенного списка начинается с отступа из пробелов, на 4 пробела большего,
 * чем список-родитель. Максимально глубина вложенности списков может достигать 6. "Верхние" списки файла начинются
 * прямо с начала строки.
 *
 * Следует вывести этот же текст в выходной файл в формате HTML:
 * Нумерованный список:
 * <ol>
 *     <li>Раз</li>
 *     <li>Два</li>
 *     <li>Три</li>
 * </ol>
 *
 * Ненумерованный список:
 * <ul>
 *     <li>Раз</li>
 *     <li>Два</li>
 *     <li>Три</li>
 * </ul>
 *
 * Кроме того, весь текст целиком следует обернуть в теги <html><body>...</body></html>
 *
 * Все остальные части исходного текста должны остаться неизменными с точностью до наборов пробелов и переносов строк.
 *
 * Пример входного файла:
///////////////////////////////начало файла/////////////////////////////////////////////////////////////////////////////
* Утка по-пекински
    * Утка
    * Соус
* Салат Оливье
    1. Мясо
        * Или колбаса
    2. Майонез
    3. Картофель
    4. Что-то там ещё
* Помидоры
* Фрукты
    1. Бананы
    23. Яблоки
        1. Красные
        2. Зелёные
///////////////////////////////конец файла//////////////////////////////////////////////////////////////////////////////
 *
 *
 * Соответствующий выходной файл:
///////////////////////////////начало файла/////////////////////////////////////////////////////////////////////////////
<html>
  <body>
    <ul>
      <li>
        Утка по-пекински
        <ul>
          <li>Утка</li>
          <li>Соус</li>
        </ul>
      </li>
      <li>
        Салат Оливье
        <ol>
          <li>Мясо
            <ul>
              <li>
                  Или колбаса
              </li>
            </ul>
          </li>
          <li>Майонез</li>
          <li>Картофель</li>
          <li>Что-то там ещё</li>
        </ol>
      </li>
      <li>Помидоры</li>
      <li>
        Яблоки
        <ol>
          <li>Красные</li>
          <li>Зелёные</li>
        </ol>
      </li>
    </ul>
  </body>
</html>
///////////////////////////////конец файла//////////////////////////////////////////////////////////////////////////////
 * (Отступы и переносы строк в примере добавлены для наглядности, при решении задачи их реализовывать не обязательно)
 */
fun markdownToHtmlLists(inputName: String, outputName: String) {
    val text = File(inputName).readLines()
    val result = File(outputName).bufferedWriter()
    val content = listToHtml(text, 0, 0).first

    result.write("<html><body>$content</body></html>")
    result.close()
}

/**
 * Вспомогательная
 * Считает количество пробелов в начале слова
 */
fun spaceCount(line: String): Int {
    var count = 0

    while (line[count] == ' ') {
        count++
    }

    return count
}

/**
 * Вспомогательная
 * Находит все списки и переводит их в формат HTML
 */
fun listToHtml(text: List<String>, nestedLevel: Int, spaceCount: Int): Pair<String,Int> {
    var result = ""
    var step = nestedLevel
    var numberedHasPair = false
    var unnumberedHasPair = false
    var newLineHasPair = false

    //Закрывает список, когда он закончится
    fun closeList() {
        if (newLineHasPair)
            result += "</li>"

        if (unnumberedHasPair)
            result += "</ul>"

        if (numberedHasPair)
            result += "</ol>"
    }

    while (step <= text.size - 1) {
        var line = text[step]

        //Если в начале строки нет указания на список, то переходим к следующей
        if (line[0] != '*' && !line.startsWith("    ") && line.substringBefore('.') !in "0".."9") {
            result += line
            step++

            continue
        }

        //Если список является вложенным, то вызываем заново функцию, сохраняя состояние
        //(то есть рассматриваем вложенный список как отдельный)
        if (spaceCount(line) == spaceCount + 4) {
            val nestedList = listToHtml(text, step, spaceCount(line))

            result += nestedList.first
            step = nestedList.second
        }
        else
        //Если вложенный список закончился, то возвращаемся к списку-родителю
            if (spaceCount(line) == spaceCount - 4) {
                closeList()

                return result to step
            }

        //Убираем пробелы по краям (необходимо для вложенных списков)
        if (spaceCount == spaceCount(line))
            line = line.trim()

        //Пробегаемся по началу строк, если встречаем там указание на список, то переводим его в формат HTML
        when {
            line[0] == '*' -> {
                if (numberedHasPair) {
                    result += "</ol>"
                    numberedHasPair = false
                }

                if (!unnumberedHasPair) {
                    result += "<ul>"
                    unnumberedHasPair = true
                }

                if (newLineHasPair) {
                    result += "</li>"
                }

                result += "<li>" + line.substringAfter('*')
                newLineHasPair = true
                step++
            }
            line.substringBefore('.') in "0".."9" -> {
                if (unnumberedHasPair) {
                    result += "</ul>"
                    unnumberedHasPair = false
                }

                if (!numberedHasPair) {
                    result += "<ol>"
                    numberedHasPair = true
                }

                if (newLineHasPair)
                    result += "</li>"

                result += "<li>" + line.substringAfter('.')
                newLineHasPair = true
                step++
            }
            else -> {/*NOTHING*/
            }
        }
    }

    closeList()

    return result to step
}

/**
 * Очень сложная
 *
 * Реализовать преобразования из двух предыдущих задач одновременно над одним и тем же файлом.
 * Следует помнить, что:
 * - Списки, отделённые друг от друга пустой строкой, являются разными и должны оказаться в разных параграфах выходного файла.
 *
 */
fun markdownToHtml(inputName: String, outputName: String) {
    val text = File(inputName).readLines()
    val result = File(outputName).bufferedWriter()
    val paragraphList = findAllParagraphs(text)
    val listToHtml = listToHtml(paragraphList, 0, 0).first
    val textToHtml = textToHtml(listToHtml)
    //Если первая строка в тексте - список, то параграфы не ставим (это кстати очень странно)
    val basic = if (text[0][0].toString().contains(Regex("""\d|\*""")))
        "<html><body>$textToHtml</body></html>"
    else
        "<html><body><p>$textToHtml</p></body></html>"

    result.write(basic)
    result.close()
}

/**
 * Средняя
 *
 * Вывести в выходной файл процесс умножения столбиком числа lhv (> 0) на число rhv (> 0).
 *
 * Пример (для lhv == 19935, rhv == 111):
   19935
*    111
--------
   19935
+ 19935
+19935
--------
 2212785
 * Используемые пробелы, отступы и дефисы должны в точности соответствовать примеру.
 * Нули в множителе обрабатывать так же, как и остальные цифры:
  235
*  10
-----
    0
+235
-----
 2350
 *
 */
fun printMultiplicationProcess(lhv: Int, rhv: Int, outputName: String) {
    TODO()
}


/**
 * Сложная
 *
 * Вывести в выходной файл процесс деления столбиком числа lhv (> 0) на число rhv (> 0).
 *
 * Пример (для lhv == 19935, rhv == 22):
  19935 | 22
 -198     906
 ----
    13
    -0
    --
    135
   -132
   ----
      3

 * Используемые пробелы, отступы и дефисы должны в точности соответствовать примеру.
 *
 */
fun printDivisionProcess(lhv: Int, rhv: Int, outputName: String) {
    TODO()
}

