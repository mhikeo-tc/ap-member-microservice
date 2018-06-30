SELECT
    p.user_id AS userId,
    SUM(gross_amount) AS amount,
    psl.payment_status_desc AS status
FROM
    informixoltp\:payment p
    INNER JOIN informixoltp\:payment_detail pd ON p.most_recent_detail_id = pd.payment_detail_id
    INNER JOIN informixoltp\:payment_status_lu psl ON pd.payment_status_id = psl.payment_status_id
               AND psl.payment_status_id NOT IN (65, 69) AND payment_status_active_ind = 1
    INNER JOIN informixoltp\:payment_type_lu ptl ON pd.payment_type_id = ptl.payment_type_id AND ptl.payment_type_id NOT IN (3, 5)
WHERE
    p.user_id = :loggedInUserId
GROUP BY
    p.user_id, psl.payment_status_desc