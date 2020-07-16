package com.tonyleiva.ufrgs.data.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.tonyleiva.ufrgs.data.GlossaryCollection;

@Service
public interface GlossaryRepossitory extends JpaRepository<GlossaryCollection, Long> {

	List<GlossaryCollection> findAll();

	Optional<GlossaryCollection> findById(Long id);

	List<GlossaryCollection> findByTerm(String term);

}
