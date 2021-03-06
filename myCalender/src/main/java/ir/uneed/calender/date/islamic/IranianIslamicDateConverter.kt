package ir.uneed.calender.date.islamic

import java.util.*
import kotlin.math.ceil

object IranianIslamicDateConverter {
    private val yearsMonthsInJd: MutableMap<Int, LongArray?> = HashMap()
    private var supportedYearsStart = 0
    private val yearsStartJd: LongArray?
    private var jdSupportEnd: Long = 0
    private const val jdSupportStart: Long = 2425063 // CivilDate(1927, 7, 1).toJdn()
    var latestSupportedYearOfIran = 1400
    fun toJdn(year: Int, month: Int, day: Int): Long {
        if (yearsMonthsInJd[year] == null) return -1
        val calculatedDay = yearsMonthsInJd[year]!![month - 1]
        return if (calculatedDay == 0L) -1 else calculatedDay + day
    }

    private fun searchYearsStarts(r: Long): Int {
        var i = ((r - yearsStartJd!![0]) / (30 * 12)).toInt()
        while (i < yearsStartJd.size && yearsStartJd[i] < r) ++i
        return i
    }

    private fun searchInOneYear(yearJdn: LongArray, r: Long): Int {
        var i = ((r - yearJdn[0]) / 30).toInt()
        while (i < yearJdn.size && yearJdn[i] < r) ++i
        return i
    }

    fun fromJdn(jd: Long): IntArray? {
        if (jd < jdSupportStart || jd >= jdSupportEnd || yearsStartJd == null) return null
        val yearIndex = searchYearsStarts(jd)
        val year = yearIndex + supportedYearsStart - 1
        val yearMonths = yearsMonthsInJd[year] ?: return null
        val month = searchInOneYear(yearMonths, jd)
        if (yearMonths[month - 1] == 0L) return null
        val day = (jd - yearMonths[month - 1]).toInt()
        return intArrayOf(year, month, day)
    }

    init {
        // https://github.com/ilius/starcal/blob/master/scal3/cal_types/hijri-monthes.json
        // https://calendar.ut.ac.ir/Fa/News/Data/Doc/Calendar%201398-%201.pdf
        val hijriMonths = intArrayOf(
            1346, 29, 30, 30, 29, 30, 30, 30, 29, 29, 30, 29, 29,
            1347, 30, 29, 30, 30, 29, 30, 30, 29, 30, 29, 30, 29,
            1348, 29, 30, 29, 30, 30, 29, 30, 30, 29, 30, 29, 30,
            1349, 29, 29, 30, 29, 30, 29, 30, 30, 29, 30, 30, 29,
            1350, 30, 29, 29, 30, 30, 29, 29, 30, 29, 30, 30, 29,
            1351, 30, 30, 29, 30, 29, 30, 29, 29, 30, 29, 30, 29,
            1352, 30, 30, 29, 30, 30, 30, 29, 29, 29, 30, 29, 30,
            1353, 29, 30, 30, 30, 29, 30, 29, 30, 29, 29, 30, 29,
            1354, 29, 30, 30, 30, 29, 30, 30, 29, 29, 30, 29, 30,
            1355, 29, 29, 30, 30, 29, 30, 30, 29, 30, 29, 30, 29,
            1356, 30, 29, 29, 30, 30, 29, 30, 29, 30, 30, 29, 30,
            1357, 29, 30, 29, 30, 29, 30, 29, 29, 30, 30, 29, 30,
            1358, 30, 29, 30, 29, 30, 29, 29, 30, 29, 30, 29, 30,
            1359, 30, 30, 29, 30, 29, 30, 29, 29, 30, 29, 29, 30,
            1360, 30, 30, 29, 30, 30, 29, 30, 29, 29, 30, 29, 30,
            1361, 29, 30, 29, 30, 30, 29, 30, 30, 29, 29, 30, 29,
            1362, 30, 29, 30, 29, 30, 30, 29, 30, 29, 30, 29, 30,
            1363, 29, 30, 29, 30, 29, 30, 29, 30, 29, 30, 30, 29,
            1364, 30, 29, 30, 29, 29, 30, 29, 30, 29, 30, 30, 30,
            1365, 29, 30, 29, 30, 29, 29, 30, 29, 30, 29, 30, 30,
            1366, 29, 30, 30, 29, 30, 29, 29, 30, 29, 30, 29, 30,
            1367, 30, 29, 30, 30, 29, 30, 29, 29, 30, 29, 30, 29,
            1368, 30, 29, 30, 30, 29, 30, 30, 29, 29, 30, 29, 30,
            1369, 29, 30, 29, 30, 29, 30, 30, 29, 30, 29, 30, 30,
            1370, 29, 29, 30, 29, 30, 29, 30, 29, 30, 30, 29, 30,
            1371, 29, 30, 29, 30, 29, 30, 29, 29, 30, 30, 30, 29,
            1372, 30, 30, 29, 29, 30, 29, 29, 30, 29, 30, 30, 30,
            1373, 29, 30, 29, 30, 29, 30, 29, 29, 30, 29, 30, 30,
            1374, 29, 30, 30, 29, 30, 29, 30, 29, 29, 30, 29, 30,
            1375, 29, 30, 30, 29, 30, 30, 29, 30, 29, 29, 30, 29,
            1376, 30, 29, 30, 29, 30, 30, 29, 30, 30, 29, 29, 30,
            1377, 29, 30, 29, 29, 30, 30, 29, 30, 30, 30, 29, 30,
            1378, 29, 29, 30, 29, 29, 30, 29, 30, 30, 30, 29, 30,
            1379, 30, 29, 29, 30, 29, 29, 30, 29, 30, 30, 29, 30,
            1380, 30, 29, 30, 29, 30, 29, 29, 30, 29, 30, 29, 30,
            1381, 30, 29, 30, 30, 29, 30, 29, 30, 29, 29, 30, 29,
            1382, 30, 29, 30, 30, 29, 30, 30, 29, 30, 29, 29, 30,
            1383, 29, 29, 30, 30, 29, 30, 30, 30, 29, 30, 29, 29,
            1384, 30, 29, 29, 30, 29, 30, 30, 30, 29, 30, 30, 29,
            1385, 29, 30, 29, 29, 30, 29, 30, 30, 29, 30, 30, 30,
            1386, 29, 29, 30, 29, 29, 30, 29, 30, 29, 30, 30, 30,
            1387, 29, 30, 29, 30, 29, 30, 29, 29, 30, 29, 30, 30,
            1388, 29, 30, 30, 29, 30, 29, 30, 29, 29, 30, 29, 30,
            1389, 29, 30, 30, 29, 30, 30, 29, 30, 29, 29, 30, 29,
            1390, 30, 29, 30, 29, 30, 30, 30, 29, 30, 29, 30, 29,
            1391, 29, 30, 29, 29, 30, 30, 30, 29, 30, 30, 29, 30,
            1392, 29, 29, 30, 29, 29, 30, 30, 29, 30, 30, 29, 30,
            1393, 30, 29, 29, 30, 29, 29, 30, 30, 29, 30, 29, 30,
            1394, 30, 30, 29, 30, 29, 29, 30, 29, 29, 30, 29, 30,
            1395, 30, 30, 29, 30, 29, 30, 29, 30, 29, 29, 29, 30,
            1396, 30, 29, 30, 30, 30, 29, 30, 29, 30, 29, 29, 30,
            1397, 29, 30, 29, 30, 30, 30, 29, 30, 29, 30, 29, 29,
            1398, 30, 29, 30, 29, 30, 29, 30, 30, 29, 30, 29, 30,
            1399, 29, 30, 29, 30, 29, 29, 30, 30, 29, 30, 30, 29,
            1400, 30, 29, 30, 29, 30, 29, 29, 30, 29, 30, 30, 30,
            1401, 29, 30, 30, 29, 29, 30, 29, 29, 30, 29, 30, 29,
            1402, 30, 30, 30, 29, 30, 29, 30, 29, 29, 30, 29, 30,
            1403, 29, 30, 30, 30, 29, 30, 29, 30, 29, 29, 30, 29,
            1404, 30, 29, 30, 30, 30, 29, 30, 29, 30, 29, 29, 30,
            1405, 29, 30, 29, 30, 30, 29, 30, 30, 29, 30, 29, 30,
            1406, 29, 29, 30, 29, 30, 29, 30, 30, 29, 30, 29, 30,
            1407, 30, 29, 30, 29, 29, 30, 29, 30, 29, 30, 30, 29,
            1408, 30, 30, 29, 30, 29, 29, 30, 29, 29, 30, 30, 29,
            1409, 30, 30, 30, 29, 29, 30, 29, 30, 29, 29, 30, 30,
            1410, 29, 30, 30, 29, 30, 29, 30, 29, 30, 29, 29, 30,
            1411, 30, 30, 29, 30, 29, 30, 29, 30, 29, 30, 29, 29,
            1412, 30, 30, 29, 30, 29, 30, 29, 30, 29, 30, 30, 29,
            1413, 30, 29, 30, 29, 29, 30, 29, 30, 29, 30, 30, 30,
            1414, 29, 30, 29, 29, 30, 29, 30, 29, 29, 30, 30, 30,
            1415, 30, 30, 29, 29, 29, 30, 29, 29, 29, 30, 30, 30,
            1416, 30, 30, 29, 30, 29, 29, 30, 29, 29, 30, 30, 29,
            1417, 30, 30, 30, 29, 29, 30, 29, 30, 29, 30, 29, 29,
            1418, 30, 30, 29, 30, 30, 29, 30, 29, 29, 30, 30, 29,
            1419, 29, 30, 29, 30, 29, 30, 30, 29, 29, 30, 30, 30,
            1420, 29, 29, 30, 29, 30, 29, 30, 30, 29, 30, 30, 29,
            1421, 30, 29, 29, 30, 29, 29, 30, 30, 29, 30, 30, 30,
            1422, 29, 30, 29, 29, 30, 29, 29, 30, 29, 30, 30, 30,
            1423, 29, 30, 30, 29, 29, 30, 29, 30, 29, 30, 29, 30,
            1424, 30, 29, 30, 29, 30, 29, 30, 29, 30, 29, 30, 29,
            1425, 30, 29, 30, 30, 29, 30, 30, 29, 29, 30, 29, 30,
            1426, 29, 29, 30, 29, 30, 30, 30, 29, 30, 30, 29, 29,
            1427, 30, 29, 29, 30, 29, 30, 30, 30, 29, 30, 29, 30,
            1428, 29, 30, 29, 29, 29, 30, 30, 29, 30, 30, 30, 29,
            1429, 30, 29, 30, 29, 29, 29, 30, 30, 29, 30, 30, 29,
            1430, 30, 30, 29, 29, 30, 29, 30, 29, 29, 30, 30, 29,
            1431, 30, 30, 29, 30, 29, 30, 29, 30, 29, 29, 30, 29,
            1432, 30, 30, 29, 30, 30, 30, 29, 30, 29, 29, 30, 29,
            1433, 29, 30, 29, 30, 30, 30, 29, 30, 29, 30, 29, 30,
            1434, 29, 29, 30, 29, 30, 30, 29, 30, 30, 29, 30, 29,
            1435, 29, 30, 29, 30, 29, 30, 29, 30, 30, 30, 29, 30,
            1436, 29, 30, 29, 29, 30, 29, 30, 29, 30, 29, 30, 30,
            1437, 29, 30, 30, 29, 30, 29, 29, 30, 29, 29, 30, 30,
            1438, 29, 30, 30, 30, 29, 30, 29, 29, 30, 29, 29, 30,
            1439, 29, 30, 30, 30, 30, 29, 30, 29, 29, 30, 29, 29,
            1440, 30, 29, 30, 30, 30, 29, 30, 30, 29, 29, 30, 29,
            1441, 29, 30, 29, 30, 30, 29, 30, 30, 29, 30, 29, 30,
            1442, 29, 29, 30, 29, 30, 29, 30, 30, 29, 30, 30, 29,
            1443, 29, 30, 30, 29, 29, 30, 29,  /**/30, 29, 30, 29, 30
        )
        val years = ceil((hijriMonths.size.toFloat() / 13).toDouble()).toInt()
        yearsStartJd = LongArray(years)
        supportedYearsStart = hijriMonths[0]
        var jd = jdSupportStart - 1
        for (y in 0 until years) {
            val year: Int = hijriMonths[y * 13]
            yearsStartJd[y] = jd
            val months = LongArray(12)
            var m = 1
            while (m < 13 && y * 13 + m < hijriMonths.size) {
                months[m - 1] = jd
                jd += hijriMonths[y * 13 + m]
                ++m
            }
            yearsMonthsInJd[year] = months
        }
        jdSupportEnd = jd
    }
}