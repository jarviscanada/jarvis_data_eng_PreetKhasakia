
SELECT	cpu_number,
       	id,
       	total_mem

FROM 
	PUBLIC.host_info

GROUP BY 
	cpu_number, 
	id 

ORDER BY total_mem DESC;

CREATE FUNCTION round5(ts timestamp) RETURNS timestamp AS
$$
BEGIN
	RETURN date_trunc('hour', ts) + date_part('minute', ts):: int / 5 * interval '5 min';
END
$$ LANGUAGE PLPGSQL;


--SELECT host_id, timestamp, round5(timestamp)
--FROM host_usage;


SELECT 	u.host_id, 
	i.hostname, 
	round5(u.timestamp), 
	AVG(((i.total_mem - u.memory_free)*100)/i.total_mem) AS avg_used_memory

FROM host_info AS i
LEFT JOIN host_usage as u ON i.id=u.host_id
GROUP BY u.host_id, i.hostname, round5(u.timestamp);

--SELECT host_id, round5(timestamp),
--COUNT(*) AS data
--FROM host_usage

--IF data < 3 THEN
--		GROUP BY host_id, timestamp;
--END IF;



