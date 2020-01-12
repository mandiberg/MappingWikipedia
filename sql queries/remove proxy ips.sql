DELETE ipinfos FROM ipinfos
INNER JOIN proxy ON ipinfos.ip_id = proxy.ip;
