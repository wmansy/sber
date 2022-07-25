1. —колько строк и полей и почему вернет SQL запрос Уselect * from A,B,CФ, если
а) в A содержит 3 строки, B - 4 строки, а C - 5 строк?
	ѕри перечеслении таблиц, количество строк перемножитс€. ќтвет: 60

б) в A содержит 0 строк, B - 1 строка, а C - 2 строки?
	ќтвет: 0

2. ѕредложите варианты отбора уникальных значений пол€ УnameФ из таблицы УAФ (в —”Ѕƒ только эта одна таблица).
Ќеобходимо минимум 4 различных варианта (известно минимум 7 разных вариантов).

    1) select name from A group by name
	2) select distinct name from A
	3) select name from A
	   union
	   select name from A
	4)select name from (
    		select name, row_number() over (partition by name) as cnt from dq_rule) as b
	  where cnt = 1;

3. Ќапишите SQL, использу€ таблицу УAФ, возвращающий ровно 100 строк со значени€ми чисел от 1 до 100 в каждой строке.
ќграничени€: 1) не использовать connect by; 2) не использовать union/union all; 3) в запросе можно использовать только таблицу УAФ,
в которой ровно одно поле УCOLУ с текстовым значением СXТ, и в таблице УAФ содержитс€ 7 строк.

	SELECT row_number
	FROM (
		SELECT row_number() over () as row_number
		FROM "A", "A" as a1, "A" as a2) as b
	LIMIT 100

4. ѕредложите SQL (без использовани€ PIVOT) дл€ получени€ клиентских атрибутов (таблица: ATTR; пол€: CLIENT_ID, PARAM_NAME, PARAM_VALUE;
первичный ключ: ID, PARAM_NAME; всего различных параметров 100: от Сp1Т до Сp100Т) в строку (то есть один клиент Ц одна строка).
Ќа выходе должно быть 101 поле: CLIENT_ID, P_1, Е, P_100.

	SELECT * FROM crosstab('select "CLIENT_ID", "PARAM_VALUE", "PARAM_NAME" from "ATTR" order by 1,2')
             AS ct ("CLIENT_ID" integer,
                    "P_1" text,"P_2" text, "P_3" text, "P_4" text, "P_5" text,
                    "P_6" text,"P_7" text, "P_8" text, "P_9" text, "P_10" text,
                    "P_11" text,"P_12" text, "P_13" text, "P_14" text, "P_15" text,
                    "P_16" text,"P_17" text, "P_18" text, "P_19" text, "P_20" text,
                    "P_21" text,"P_22" text, "P_23" text, "P_24" text, "P_25" text,
                    "P_26" text,"P_27" text, "P_28" text, "P_29" text, "P_30" text,
                    "P_31" text,"P_32" text, "P_33" text, "P_34" text, "P_35" text,
                    "P_36" text,"P_37" text, "P_38" text, "P_39" text, "P_40" text,
                    "P_41" text,"P_42" text, "P_43" text, "P_44" text, "P_45" text,
                    "P_46" text,"P_47" text, "P_48" text, "P_49" text, "P_50" text,
                    "P_51" text,"P_52" text, "P_53" text, "P_54" text, "P_55" text,
                    "P_56" text,"P_57" text, "P_58" text, "P_59" text, "P_60" text,
                    "P_61" text,"P_62" text, "P_63" text, "P_64" text, "P_65" text,
                    "P_66" text,"P_67" text, "P_68" text, "P_69" text, "P_70" text,
                    "P_71" text,"P_72" text, "P_73" text, "P_74" text, "P_75" text,
                    "P_76" text,"P_77" text, "P_78" text, "P_79" text, "P_80" text,
                    "P_81" text,"P_82" text, "P_83" text, "P_84" text, "P_85" text,
                    "P_86" text,"P_87" text, "P_88" text, "P_89" text, "P_90" text,
                    "P_91" text,"P_92" text, "P_93" text, "P_94" text, "P_95" text,
                    "P_96" text,"P_97" text, "P_98" text, "P_99" text, "P_100" text);

5. ≈сть таблица:
create table t(col varchar2(3));
insert into t(col) values('AAA');
insert into t(col) values('BBB');
insert into t(col) values(null);
insert into t(col) values('BBB');
insert into t(col) values('BBB');
insert into t(col) values('CCC');
commit;
”далите дубликаты из неЄ без промежуточных таблиц, одним запросом.

	DELETE FROM t a
	WHERE a.ctid <> (SELECT min(b.ctid) FROM t b WHERE  a.col = b.col);

6. ≈сть таблица лога выполнени€ некой операции. ЌазовЄм еЄ log.
ѕол€ таблицы:
    oper_id Ц идентификатор операции
    start_ts Ц дата и врем€ начала операции с точностью до секунды
    end_ts Ц дата и врем€ окончани€ операции с точностью до секунды
ќперации могут пересекатьс€ по времени. “о есть, какие-то из них работают параллельно.
¬ таблице лежат данные за несколько дней.
Ќеобходимо посчитать среднее количество одновременно выполн€вшихс€ операций по часам.
“о есть на выходе получить примерно:
08:00 Ц 0 операций
09:00 Ц 2 операции
10:00 Ц 50 операций
11:00 Ц 42 операции
12:00 Ц 84 операции
13:00 Ц 11 операций
» .тд.

with days as (
    select to_char(max(end_ts) - min(start_ts), 'DD')::integer as nday from log2)

select time, count(oper_id)/nday as avg_oper
from (
    select to_char(start_ts, 'HH24:00') as time, oper_id from log2
    union
    select to_char(end_ts, 'HH24:00'), oper_id from log2) as b, days
group by time, nday
order by time
