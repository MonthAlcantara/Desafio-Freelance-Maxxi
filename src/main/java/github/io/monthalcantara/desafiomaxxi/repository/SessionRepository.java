package github.io.monthalcantara.desafiomaxxi.repository;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpSession;
import java.util.List;

@Repository
public class SessionRepository {

    private HashOperations hashOperations;
    private RedisTemplate redisTemplate;

    public SessionRepository(RedisTemplate redisTemplate) {
        this.hashOperations=redisTemplate.opsForHash();
        this.redisTemplate = redisTemplate;
    }

    public void saveEmployee(HttpSession session){
        hashOperations.put("Session:",session.getId(),session);
    }

    public List<HttpSession> findAll(){
        return hashOperations.values("Session");
    }

    public HttpSession findById(String id){
        return (HttpSession) hashOperations.get("Session",id);

    }
    public void update(HttpSession session){
        saveEmployee(session);
    }
    public void delete(String id){
        hashOperations.delete("Session",id);
    }
}
