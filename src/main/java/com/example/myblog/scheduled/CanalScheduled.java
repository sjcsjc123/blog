package com.example.myblog.scheduled;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.example.myblog.domain.IndexBlog;
import com.example.myblog.elasticsearch.entity.IndexBlogEs;
import com.example.myblog.elasticsearch.mapper.SearchMapper;
import com.example.myblog.mapper.HistoryMapper;
import com.example.myblog.vo.CanalRowDataVo;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author SJC
 */
@Service
public class CanalScheduled {

    @Autowired
    private SearchMapper searchMapper;
    @Autowired
    private HistoryMapper historyMapper;

    @Scheduled(cron = "* * * * * ?")
    public void StartService() throws InvalidProtocolBufferException {
        //创建链接
        CanalConnector canalConnector = CanalConnectors.newSingleConnector(
                new InetSocketAddress("localhost", 11111),
                "example", "", "");
        while (true) {
            //获取链接
            canalConnector.connect();
            //监控数据库
            canalConnector.subscribe("blog.index_blog");
            //获取message
            Message message = canalConnector.get(100);
            //获取entry
            List<CanalEntry.Entry> entries = message.getEntries();

            if (entries.size() <= 0) {
            } else {
                for (CanalEntry.Entry entry : entries) {
                    //操作类型
                    CanalEntry.EntryType entryType = entry.getEntryType();
                    if (entryType.equals(CanalEntry.EntryType.ROWDATA)) {
                        //序列化数据
                        ByteString storeValue = entry.getStoreValue();
                        //反序列化
                        CanalEntry.RowChange rowChange =
                                CanalEntry.RowChange.parseFrom(storeValue);
                        //事件类型
                        CanalEntry.EventType eventType = rowChange.getEventType();
                        if (eventType.equals(CanalEntry.EventType.INSERT)) {
                            insertSql(rowChange);
                        } else if (eventType.equals(CanalEntry.EventType.DELETE)) {
                            deleteSql(rowChange);
                        } else if (eventType.equals(CanalEntry.EventType.UPDATE)) {
                            updateSql(rowChange);
                        }
                    }
                }
            }
        }
    }

    private void updateSql(CanalEntry.RowChange rowChange) {

    }

    private void deleteSql(CanalEntry.RowChange rowChange) {
        List<CanalRowDataVo> beforeAndAfterData =
                getBeforeAndAfterData(rowChange);
        for (CanalRowDataVo beforeAndAfterDatum : beforeAndAfterData) {
            IndexBlog indexBlog =
                    beforeAndAfterDatum.getBefore().toJavaObject(IndexBlog.class);
            searchMapper.deleteById(indexBlog.getId());
            historyMapper.deleteById(indexBlog.getId().toString());
        }
    }

    private void insertSql(CanalEntry.RowChange rowChange) {
        List<CanalRowDataVo> rowDataVoList =
                getBeforeAndAfterData(rowChange);
        for (CanalRowDataVo canalRowDataVo : rowDataVoList) {
            JSONObject after = canalRowDataVo.getAfter();
            IndexBlog indexBlog = after.toJavaObject(IndexBlog.class);
            IndexBlogEs indexBlogEs = new IndexBlogEs();
            indexBlogEs.setTitle(indexBlog.getTitle());
            indexBlogEs.setId(indexBlog.getId());
            indexBlogEs.setDescription(indexBlog.getDescription());
            indexBlogEs.setWeight(indexBlog.getWeight());
            indexBlogEs.setCreateTime(indexBlog.getCreateTime());
            indexBlogEs.setAuthor(indexBlog.getAuthor());
            indexBlogEs.setCommentNum(0);
            indexBlogEs.setStarNum(0);
            indexBlogEs.setThumbUpNum(0);
            indexBlogEs.setVisitNum(0);
            searchMapper.save(indexBlogEs);
        }
    }

    /**
     * 提出一个公共方法，根据rowChange获取before和after数据
     *
     * @param rowChange
     */
    public List<CanalRowDataVo> getBeforeAndAfterData(CanalEntry.RowChange rowChange) {
        List<CanalEntry.RowData> rowDataList =
                rowChange.getRowDatasList();
        List<CanalRowDataVo> rowDataVoList = new ArrayList<>();
        for (CanalEntry.RowData rowData : rowDataList) {
            //获取执行sql语句之前的数据
            JSONObject before = new JSONObject();
            List<CanalEntry.Column> beforeColumnsList =
                    rowData.getBeforeColumnsList();
            for (CanalEntry.Column column : beforeColumnsList) {
                before.put(column.getName(), column.getValue());
            }
            //获取执行sql执行之后的数据
            JSONObject after = new JSONObject();
            List<CanalEntry.Column> afterColumnsList =
                    rowData.getAfterColumnsList();
            for (CanalEntry.Column column : afterColumnsList) {
                after.put(column.getName(), column.getValue());
            }
            rowDataVoList.add(new CanalRowDataVo(before,after));
        }
        return rowDataVoList;
    }
}
