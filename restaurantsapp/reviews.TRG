TYPE=TRIGGERS
triggers='CREATE DEFINER=`root`@`localhost` TRIGGER update_avg_rating AFTER INSERT ON reviews\nFOR EACH ROW BEGIN\n    DECLARE oldCount INT;\n    SET oldCount = (SELECT  count(*)\n                    FROM    serves\n                    WHERE   dID = NEW.dID);\n    UPDATE dishes SET avgRating = ((avgRating*oldCount) + NEW.rating) / (oldCount + 1) WHERE ID = NEW.dID;\nEND'
sql_modes=1073741824
definers='root@localhost'
client_cs_names='cp850'
connection_cl_names='cp850_general_ci'
db_cl_names='latin1_swedish_ci'
