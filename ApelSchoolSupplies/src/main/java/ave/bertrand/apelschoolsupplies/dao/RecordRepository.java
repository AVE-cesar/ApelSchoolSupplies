package ave.bertrand.apelschoolsupplies.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ave.bertrand.apelschoolsupplies.model.Record;

@Repository
public interface RecordRepository extends CrudRepository<Record, Long> {

	Iterable<Record> findByClassNumberOrderByStudentNameAsc(String classNumber);
}
