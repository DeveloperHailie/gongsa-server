package study.gongsa.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import study.gongsa.domain.Answer;
import study.gongsa.domain.AnswerInfo;
import study.gongsa.domain.Category;
import study.gongsa.domain.Question;

import java.sql.Timestamp;
import java.util.*;

@Repository
public class JdbcTemplateAnswerRepository implements AnswerRepository{
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert insertIntoAnswer;

    public JdbcTemplateAnswerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertIntoAnswer = new SimpleJdbcInsert(jdbcTemplate).withTableName("Answer").usingGeneratedKeyColumns("UID");
    }

    @Override
    public Number save(Answer answer) {
        final Map<String, Object> parameters = setParameter(answer);
        return insertIntoAnswer.executeAndReturnKey(parameters);
    }

    @Override
    public Optional<Answer> findOne(int UID) {
        List<Answer> result = jdbcTemplate.query("select * from Answer where UID = ?", answerRowMapper(), UID);
        return result.stream().findAny();
    }

    @Override
    public void update(int UID, String content) {
        String sql = "update Answer set answer=? , updatedAt=now()" + " where UID=?";
        jdbcTemplate.update(sql, content, UID);
    }

    @Override
    public void deleteUserAnswer(List<Integer> questionUIDs, int userUID) {
        List<Integer> sqlData = questionUIDs;
        String inSql = String.join(",", Collections.nCopies(questionUIDs.size(), "?"));
        String query = String.format("DELETE FROM Answer WHERE questionUID in (%s) AND userUID = ?", inSql);
        sqlData.add(userUID);
        jdbcTemplate.update(query, sqlData.toArray());
    }

    @Override
    public void remove(int UID) {
        String sql = "delete from Answer where uid = ?";
        jdbcTemplate.update(sql, UID);
    }

    @Override
    public List<AnswerInfo> findAnswer(int questionUID) {
        String sql = "SELECT a.UID, a.userUID, c.nickname, a.answer, a.createdAt "
                + "FROM Answer a "
                + "JOIN Question b ON b.UID = a.questionUID "
                + "JOIN User c ON c.UID = a.userUID "
                + "WHERE a.questionUID = ?";
        System.out.println(sql);
        return jdbcTemplate.query(sql, answerInfoRowMapper(), questionUID);
    }

    private RowMapper<AnswerInfo> answerInfoRowMapper() {
        return (rs, rowNum) -> {
            AnswerInfo answer = new AnswerInfo();
            answer.setUID(rs.getInt("UID"));
            answer.setUserUID(rs.getInt("userUID"));
            answer.setNickname(rs.getString("nickName"));
            answer.setAnswer(rs.getString("answer"));
            answer.setCreatedAt(rs.getTimestamp("createdAt"));
            return answer;
        };
    }

    private RowMapper<Answer> answerRowMapper() {
        return (rs, rowNum) -> {
            Answer answer = new Answer();
            answer.setUID(rs.getInt("UID"));
            answer.setQuestionUID(rs.getInt("questionUID"));
            answer.setUserUID(rs.getInt("userUID"));
            answer.setAnswer(rs.getString("answer"));
            answer.setCreatedAt(rs.getTimestamp("createdAt"));
            answer.setUpdatedAt(rs.getTimestamp("updatedAt"));
            return answer;
        };
    }

    private HashMap<String, Object> setParameter(Answer answer) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("UID",answer.getUID());
        hashMap.put("questionUID",answer.getQuestionUID());
        hashMap.put("userUID",answer.getUserUID());
        hashMap.put("answer",answer.getAnswer());
        hashMap.put("createdAt",answer.getCreatedAt());
        hashMap.put("updatedAt",answer.getUpdatedAt());

        return hashMap;
    }
}
