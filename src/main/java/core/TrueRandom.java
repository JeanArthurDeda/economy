package core;

public class TrueRandom {
    protected static int mIndex;
    protected static int[] mTable = new int[]{
            584,
            9,
            274,
            231,
            430,
            925,
            633,
            253,
            457,
            714,
            680,
            125,
            449,
            255,
            384,
            778,
            422,
            101,
            57,
            886,
            856,
            332,
            809,
            921,
            81,
            158,
            230,
            108,
            834,
            369,
            333,
            455,
            474,
            942,
            137,
            601,
            421,
            926,
            126,
            500,
            299,
            329,
            45,
            313,
            756,
            814,
            800,
            186,
            326,
            773,
            565,
            935,
            297,
            559,
            89,
            604,
            554,
            79,
            403,
            659,
            68,
            10,
            428,
            607,
            845,
            339,
            397,
            949,
            534,
            129,
            116,
            38,
            11,
            280,
            49,
            666,
            492,
            438,
            252,
            19,
            907,
            526,
            880,
            166,
            643,
            242,
            328,
            301,
            806,
            817,
            456,
            742,
            908,
            894,
            603,
            916,
            136,
            938,
            406,
            308,
            111,
            479,
            613,
            216,
            913,
            618,
            854,
            42,
            974,
            867,
            76,
            954,
            560,
            918,
            250,
            197,
            35,
            322,
            746,
            259,
            906,
            706,
            598,
            833,
            580,
            859,
            981,
            962,
            702,
            793,
            729,
            645,
            176,
            970,
            802,
            103,
            163,
            566,
            26,
            450,
            577,
            785,
            887,
            360,
            629,
            291,
            700,
            240,
            263,
            232,
            376,
            871,
            7,
            284,
            832,
            783,
            185,
            470,
            124,
            152,
            575,
            964,
            734,
            418,
            233,
            283,
            495,
            794,
            114,
            624,
            402,
            850,
            557,
            883,
            410,
            915,
            911,
            641,
            381,
            254,
            316,
            83,
            5,
            327,
            540,
            984,
            950,
            337,
            818,
            937,
            721,
            733,
            752,
            289,
            359,
            674,
            747,
            590,
            368,
            525,
            516,
            951,
            65,
            743,
            452,
            820,
            309,
            799,
            515,
            212,
            520,
            132,
            414,
            383,
            536,
            324,
            379,
            873,
            959,
            547,
            717,
            735,
            664,
            311,
            967,
            551,
            416,
            43,
            940,
            619,
            210,
            992,
            941,
            934,
            993,
            235,
            966,
            593,
            391,
            874,
            568,
            352,
            74,
            489,
            350,
            372,
            701,
            574,
            861,
            872,
            338,
            677,
            922,
            310,
            689,
            227,
            866,
            59,
            195,
            447,
            374,
            189,
            100,
            801,
            903,
            41,
            718,
            4,
            764,
            507,
            303,
            976,
            320,
            739,
            50,
            170,
            278,
            30,
            454,
            803,
            849,
            3,
            585,
            472,
            597,
            173,
            307,
            521,
            744,
            673,
            128,
            669,
            213,
            786,
            696,
            605,
            149,
            988,
            294,
            487,
            73,
            266,
            655,
            429,
            828,
            920,
            225,
            238,
            398,
            13,
            875,
            888,
            960,
            656,
            358,
            650,
            587,
            243,
            222,
            15,
            29,
            745,
            909,
            646,
            812,
            731,
            249,
            985,
            685,
            219,
            182,
            615,
            848,
            975,
            411,
            884,
            251,
            654,
            130,
            635,
            893,
            66,
            121,
            118,
            672,
            168,
            608,
            508,
            626,
            207,
            342,
            698,
            923,
            695,
            321,
            355,
            298,
            971,
            239,
            754,
            905,
            544,
            300,
            183,
            165,
            948,
            138,
            748,
            143,
            570,
            373,
            895,
            190,
            779,
            171,
            892,
            901,
            640,
            193,
            712,
            929,
            682,
            94,
            272,
            64,
            199,
            836,
            667,
            281,
            652,
            25,
            370,
            963,
            268,
            366,
            330,
            224,
            415,
            588,
            467,
            460,
            52,
            795,
            898,
            807,
            579,
            32,
            549,
            46,
            631,
            323,
            77,
            317,
            12,
            594,
            632,
            211,
            206,
            287,
            611,
            919,
            70,
            276,
            214,
            459,
            958,
            31,
            292,
            33,
            440,
            956,
            346,
            932,
            433,
            401,
            351,
            651,
            791,
            161,
            524,
            382,
            148,
            427,
            737,
            592,
            169,
            105,
            483,
            63,
            51,
            102,
            649,
            264,
            841,
            983,
            839,
            881,
            347,
            614,
            97,
            399,
            461,
            774,
            245,
            676,
            209,
            781,
            478,
            6,
            277,
            140,
            119,
            868,
            34,
            75,
            581,
            167,
            658,
            889,
            475,
            405,
            362,
            256,
            365,
            576,
            977,
            484,
            545,
            823,
            273,
            269,
            749,
            184,
            582,
            784,
            334,
            345,
            446,
            237,
            86,
            939,
            115,
            200,
            751,
            154,
            683,
            944,
            349,
            275,
            973,
            85,
            591,
            692,
            480,
            661,
            991,
            622,
            194,
            363,
            766,
            573,
            496,
            555,
            491,
            863,
            217,
            787,
            928,
            90,
            904,
            122,
            155,
            638,
            627,
            286,
            704,
            804,
            445,
            241,
            157,
            466,
            153,
            693,
            435,
            106,
            813,
            20,
            726,
            838,
            318,
            980,
            84,
            229,
            539,
            852,
            477,
            623,
            522,
            864,
            821,
            978,
            530,
            388,
            40,
            847,
            234,
            708,
            719,
            827,
            67,
            91,
            28,
            668,
            931,
            694,
            501,
            288,
            857,
            314,
            497,
            159,
            531,
            924,
            179,
            120,
            48,
            891,
            142,
            665,
            912,
            279,
            788,
            846,
            58,
            899,
            563,
            890,
            469,
            648,
            385,
            271,
            642,
            55,
            707,
            24,
            408,
            71,
            223,
            602,
            989,
            424,
            404,
            110,
            732,
            442,
            39,
            843,
            675,
            723,
            473,
            851,
            270,
            917,
            144,
            999,
            770,
            331,
            490,
            537,
            1,
            571,
            877,
            869,
            267,
            567,
            996,
            18,
            946,
            625,
            151,
            844,
            824,
            458,
            780,
            897,
            767,
            541,
            396,
            293,
            835,
            175,
            486,
            117,
            417,
            265,
            710,
            425,
            304,
            380,
            776,
            69,
            312,
            995,
            686,
            37,
            736,
            777,
            112,
            174,
            837,
            662,
            432,
            697,
            703,
            684,
            639,
            476,
            532,
            711,
            550,
            226,
            647,
            257,
            815,
            141,
            453,
            319,
            705,
            829,
            792,
            246,
            957,
            687,
            395,
            543,
            389,
            771,
            740,
            910,
            810,
            987,
            586,
            113,
            930,
            511,
            741,
            393,
            979,
            822,
            146,
            123,
            282,
            518,
            671,
            390,
            367,
            798,
            952,
            831,
            583,
            208,
            789,
            775,
            755,
            499,
            769,
            902,
            139,
            730,
            808,
            61,
            630,
            341,
            825,
            628,
            562,
            156,
            759,
            494,
            471,
            485,
            657,
            481,
            306,
            191,
            78,
            296,
            612,
            865,
            419,
            502,
            578,
            431,
            953,
            201,
            36,
            878,
            392,
            510,
            552,
            343,
            599,
            295,
            634,
            609,
            180,
            98,
            513,
            340,
            285,
            986,
            488,
            215,
            620,
            722,
            72,
            663,
            290,
            589,
            260,
            998,
            16,
            344,
            160,
            133,
            955,
            715,
            762,
            203,
            218,
            198,
            377,
            882,
            535,
            378,
            96,
            95,
            468,
            423,
            464,
            691,
            724,
            858,
            670,
            842,
            104,
            202,
            335,
            606,
            54,
            896,
            879,
            261,
            830,
            542,
            561,
            17,
            840,
            412,
            965,
            353,
            375,
            221,
            437,
            596,
            527,
            27,
            302,
            364,
            616,
            713,
            961,
            1000,
            371,
            572,
            972,
            725,
            177,
            796,
            564,
            679,
            262,
            420,
            88,
            0,
            23,
            465,
            644,
            147,
            947,
            548,
            533,
            600,
            595,
            553,
            109,
            354,
            826,
            968,
            690,
            982,
            325,
            2,
            56,
            990,
            709,
            462,
            236,
            900,
            361,
            14,
            621,
            134,
            519,
            53,
            994,
            811,
            720,
            885,
            248,
            82,
            244,
            22,
            517,
            772,
            569,
            99,
            933,
            855,
            678,
            407,
            80,
            394,
            969,
            436,
            187,
            558,
            162,
            699,
            636,
            400,
            178,
            315,
            853,
            439,
            768,
            93,
            945,
            538,
            247,
            127,
            441,
            448,
            8,
            765,
            782,
            688,
            790,
            92,
            653,
            426,
            512,
            21,
            107,
            451,
            47,
            727,
            164,
            943,
            870,
            228,
            914,
            150,
            997,
            181,
            758,
            528,
            434,
            816,
            463,
            196,
            409,
            506,
            529,
            761,
            523,
            660,
            637,
            357,
            220,
            145,
            135,
            192,
            927,
            546,
            757,
            797,
            305,
            498,
            750,
            681,
            763,
            610,
            862,
            258,
            936,
            504,
            760,
            386,
            348,
            805,
            444,
            753,
            336,
            738,
            87,
            514,
            876,
            204,
            188,
            205,
            131,
            728,
            413,
            505,
            62,
            443,
            387,
            860,
            44,
            493,
            819,
            356,
            482,
            509,
            503,
            172,
            60,
            556,
            716,
            617};

    public static double get (){
        int a = mTable[mIndex++];
        if (mIndex >= mTable.length)
            mIndex = 0;
        return (double)a / 1001.0;
    }
}
