/*
 * Copyright © 2018-today Peter M. Stahl pemistahl@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either expressed or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.pemistahl.lingua.api

import com.github.pemistahl.lingua.api.IsoCode639_1.*
import com.github.pemistahl.lingua.api.IsoCode639_3.*
import com.github.pemistahl.lingua.internal.Alphabet
import com.github.pemistahl.lingua.internal.Alphabet.*
import com.github.pemistahl.lingua.internal.Alphabet.NONE
import com.github.pemistahl.lingua.internal.util.extension.enumSetOf
import java.util.*

/**
 * The supported detectable languages.
 */
enum class Language(
    val isoCode639_1: IsoCode639_1,
    val isoCode639_3: IsoCode639_3,
    internal val alphabets: EnumSet<Alphabet>,
    internal val uniqueCharacters: String?,
) {
    AFRIKAANS(AF, AFR, enumSetOf(Alphabet.LATIN), null),
    ALBANIAN(SQ, SQI, enumSetOf(Alphabet.LATIN), null),
    AMHARIC(AM, AMH, enumSetOf(Alphabet.ETHIOPIC), null),
    ARABIC(AR, ARA, enumSetOf(Alphabet.ARABIC), null),
    ARMENIAN(HY, HYE, enumSetOf(Alphabet.ARMENIAN), null),
    AZERBAIJANI(AZ, AZE, enumSetOf(Alphabet.LATIN), "Əə"),
    BASQUE(EU, EUS, enumSetOf(Alphabet.LATIN), null),
    BELARUSIAN(BE, BEL, enumSetOf(CYRILLIC), null),
    BENGALI(BN, BEN, enumSetOf(Alphabet.BENGALI), null),
    BOKMAL(NB, NOB, enumSetOf(Alphabet.LATIN), null),
    BOSNIAN(BS, BOS, enumSetOf(Alphabet.LATIN), null),
    BULGARIAN(BG, BUL, enumSetOf(CYRILLIC), null),
    CATALAN(CA, CAT, enumSetOf(Alphabet.LATIN), "Ïï"),
    CHINESE(ZH, ZHO, enumSetOf(HAN), null),
    CROATIAN(HR, HRV, enumSetOf(Alphabet.LATIN), null),
    CZECH(CS, CES, enumSetOf(Alphabet.LATIN), "ĚěŘřŮů"),
    DANISH(DA, DAN, enumSetOf(Alphabet.LATIN), null),
    DUTCH(NL, NLD, enumSetOf(Alphabet.LATIN), null),
    ENGLISH(EN, ENG, enumSetOf(Alphabet.LATIN), null),
    ESPERANTO(EO, EPO, enumSetOf(Alphabet.LATIN), "ĈĉĜĝĤĥĴĵŜŝŬŭ"),
    ESTONIAN(ET, EST, enumSetOf(Alphabet.LATIN), null),
    FINNISH(FI, FIN, enumSetOf(Alphabet.LATIN), null),
    FRENCH(FR, FRA, enumSetOf(Alphabet.LATIN), null),
    GANDA(LG, LUG, enumSetOf(Alphabet.LATIN), null),
    GEORGIAN(KA, KAT, enumSetOf(Alphabet.GEORGIAN), null),
    GERMAN(DE, DEU, enumSetOf(Alphabet.LATIN), "ß"),
    GREEK(EL, ELL, enumSetOf(Alphabet.GREEK), null),
    GUJARATI(GU, GUJ, enumSetOf(Alphabet.GUJARATI), null),
    HEBREW(HE, HEB, enumSetOf(Alphabet.HEBREW), null),
    HINDI(HI, HIN, enumSetOf(DEVANAGARI), null),
    HUNGARIAN(HU, HUN, enumSetOf(Alphabet.LATIN), "ŐőŰű"),
    ICELANDIC(IS, ISL, enumSetOf(Alphabet.LATIN), null),
    INDONESIAN(ID, IND, enumSetOf(Alphabet.LATIN), null),
    IRISH(GA, GLE, enumSetOf(Alphabet.LATIN), null),
    ITALIAN(IT, ITA, enumSetOf(Alphabet.LATIN), null),
    JAPANESE(JA, JPN, enumSetOf(HIRAGANA, KATAKANA, HAN), null),
    KAZAKH(KK, KAZ, enumSetOf(CYRILLIC), "ӘәҒғҚқҢңҰұ"),
    KOREAN(KO, KOR, enumSetOf(HANGUL), null),
    LATIN(LA, LAT, enumSetOf(Alphabet.LATIN), null),
    LATVIAN(LV, LAV, enumSetOf(Alphabet.LATIN), "ĢģĶķĻļŅņ"),
    LITHUANIAN(LT, LIT, enumSetOf(Alphabet.LATIN), "ĖėĮįŲų"),
    MACEDONIAN(MK, MKD, enumSetOf(CYRILLIC), "ЃѓЅѕЌќЏџ"),
    MALAY(MS, MSA, enumSetOf(Alphabet.LATIN), null),
    MAORI(MI, MRI, enumSetOf(Alphabet.LATIN), null),
    MARATHI(MR, MAR, enumSetOf(DEVANAGARI), "ळ"),
    MONGOLIAN(MN, MON, enumSetOf(CYRILLIC), "ӨөҮү"),
    NYNORSK(NN, NNO, enumSetOf(Alphabet.LATIN), null),
    OROMO(OM, ORM, enumSetOf(Alphabet.LATIN), null),
    PERSIAN(FA, FAS, enumSetOf(Alphabet.ARABIC), null),
    POLISH(PL, POL, enumSetOf(Alphabet.LATIN), "ŁłŃńŚśŹź"),
    PORTUGUESE(PT, POR, enumSetOf(Alphabet.LATIN), null),
    PUNJABI(PA, PAN, enumSetOf(GURMUKHI), null),
    ROMANIAN(RO, RON, enumSetOf(Alphabet.LATIN), "Țţ"),
    RUSSIAN(RU, RUS, enumSetOf(CYRILLIC), null),
    SERBIAN(SR, SRP, enumSetOf(CYRILLIC), "ЂђЋћ"),
    SHONA(SN, SNA, enumSetOf(Alphabet.LATIN), null),
    SINHALA(SI, SIN, enumSetOf(Alphabet.SINHALA), null),
    SLOVAK(SK, SLK, enumSetOf(Alphabet.LATIN), "ĹĺĽľŔŕ"),
    SLOVENE(SL, SLV, enumSetOf(Alphabet.LATIN), null),
    SOMALI(SO, SOM, enumSetOf(Alphabet.LATIN), null),
    SOTHO(ST, SOT, enumSetOf(Alphabet.LATIN), null),
    SPANISH(ES, SPA, enumSetOf(Alphabet.LATIN), "¿¡"),
    SWAHILI(SW, SWA, enumSetOf(Alphabet.LATIN), null),
    SWEDISH(SV, SWE, enumSetOf(Alphabet.LATIN), null),
    TAGALOG(TL, TGL, enumSetOf(Alphabet.LATIN), null),
    TAMIL(TA, TAM, enumSetOf(Alphabet.TAMIL), null),
    TELUGU(TE, TEL, enumSetOf(Alphabet.TELUGU), null),
    THAI(TH, THA, enumSetOf(Alphabet.THAI), null),
    TIGRINYA(TI, TIR, enumSetOf(Alphabet.ETHIOPIC), null),
    TSONGA(TS, TSO, enumSetOf(Alphabet.LATIN), null),
    TSWANA(TN, TSN, enumSetOf(Alphabet.LATIN), null),
    TURKISH(TR, TUR, enumSetOf(Alphabet.LATIN), null),
    UKRAINIAN(UK, UKR, enumSetOf(CYRILLIC), "ҐґЄєЇї"),
    URDU(UR, URD, enumSetOf(Alphabet.ARABIC), null),
    VIETNAMESE(
        VI,
        VIE,
        enumSetOf(Alphabet.LATIN),
        "ẰằẦầẲẳẨẩẴẵẪẫẮắẤấẠạẶặẬậỀềẺẻỂểẼẽỄễẾếỆệỈỉĨĩỊịƠơỒồỜờỎỏỔổỞởỖỗỠỡỐốỚớỘộỢợƯưỪừỦủỬửŨũỮữỨứỤụỰựỲỳỶỷỸỹỴỵ",
    ),
    WELSH(CY, CYM, enumSetOf(Alphabet.LATIN), null),
    XHOSA(XH, XHO, enumSetOf(Alphabet.LATIN), null),

    // TODO for YORUBA: "E̩e̩Ẹ́ẹ́É̩é̩Ẹ̀ẹ̀È̩è̩Ẹ̄ẹ̄Ē̩ē̩ŌōO̩o̩Ọ́ọ́Ó̩ó̩Ọ̀ọ̀Ò̩ò̩Ọ̄ọ̄Ō̩ō̩ṢṣS̩s̩"
    YORUBA(YO, YOR, enumSetOf(Alphabet.LATIN), "Ṣṣ"),
    ZULU(ZU, ZUL, enumSetOf(Alphabet.LATIN), null),

    /**
     * The imaginary unknown language.
     *
     * This value is returned if no language can be detected reliably.
     */
    UNKNOWN(IsoCode639_1.NONE, IsoCode639_3.NONE, enumSetOf(NONE), null),
    ;

    companion object {
        /**
         * Returns a list of all built-in languages.
         */
        @JvmStatic
        fun all(): List<Language> = filterOutLanguages(UNKNOWN)

        /**
         * Returns a list of all built-in languages that are still spoken today.
         */
        @JvmStatic
        fun allSpokenOnes(): List<Language> = filterOutLanguages(UNKNOWN, LATIN)

        /**
         * Returns a list of all built-in languages supporting the Arabic script.
         */
        @JvmStatic
        fun allWithArabicScript(): List<Language> = values().filter { it.alphabets.contains(Alphabet.ARABIC) }

        /**
         * Returns a list of all built-in languages supporting the Cyrillic script.
         */
        @JvmStatic
        fun allWithCyrillicScript(): List<Language> = values().filter { it.alphabets.contains(CYRILLIC) }

        /**
         * Returns a list of all built-in languages supporting the Devanagari script.
         */
        @JvmStatic
        fun allWithDevanagariScript(): List<Language> = values().filter { it.alphabets.contains(DEVANAGARI) }

        /**
         * Returns a list of all built-in languages supporting the Ethiopic script.
         */
        @JvmStatic
        fun allWithEthiopicScript(): List<Language> = values().filter { it.alphabets.contains(ETHIOPIC) }

        /**
         * Returns a list of all built-in languages supporting the Latin script.
         */
        @JvmStatic
        fun allWithLatinScript(): List<Language> = values().filter { it.alphabets.contains(Alphabet.LATIN) }

        /**
         * Returns the language for the given ISO 639-1 code.
         */
        @JvmStatic
        @Suppress("ktlint:standard:function-naming")
        fun getByIsoCode639_1(isoCode: IsoCode639_1): Language = values().first { it.isoCode639_1 == isoCode }

        /**
         * Returns the language for the given ISO 639-3 code.
         */
        @JvmStatic
        @Suppress("ktlint:standard:function-naming")
        fun getByIsoCode639_3(isoCode: IsoCode639_3): Language = values().first { it.isoCode639_3 == isoCode }

        private fun filterOutLanguages(vararg languages: Language) = values().filterNot { it in languages }
    }
}
