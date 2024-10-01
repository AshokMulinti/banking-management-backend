package com.bankingmanagement.service;
import com.bankingmanagement.document.Branch;
import com.bankingmanagement.exceptions.BranchDetailsNotFoundException;
import com.bankingmanagement.model.BranchRequest;
import com.bankingmanagement.model.BranchTO;
import com.bankingmanagement.model.BranchUpdateRequest;
import com.bankingmanagement.mongoRepository.BranchMongoDbRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BranchServiceImplTest {
    @Mock
    private BranchMongoDbRepository branchMongoDbRepository;

    @InjectMocks
    private BranchServiceImpl branchService;

    @Test
    public void findAll_whenBranchDetailsExist_thenReturnBranchData() throws BranchDetailsNotFoundException {
        List<Branch> branchList = new ArrayList<>();
        Branch branch = new Branch();
        branch.setBranchId(123);
        branch.setBranchName("Main Branch");
        branch.setBranchAddress("Bangalore");
        branchList.add(branch);

        when(branchMongoDbRepository.findAll()).thenReturn(branchList);

        List<BranchTO> branchTOS = branchService.findAll();
        assertEquals(1, branchTOS.size());
    }

    @Test
    public void findAll_whenBranchDetailsNotExist_thenThrowException() {
        when(branchMongoDbRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(BranchDetailsNotFoundException.class, () -> branchService.findAll());
    }

    @Test
    public void findById_whenBranchDetailsExist_thenReturnBranchData() throws BranchDetailsNotFoundException {
        Branch branch = new Branch();
        branch.setId("123");
        branch.setBranchId(123);
        branch.setBranchName("Main Branch");
        branch.setBranchAddress("Bangalore");

        when(branchMongoDbRepository.findById("123")).thenReturn(Optional.of(branch));

        BranchTO branchTO = branchService.findById("123");
        assertEquals("123", branchTO.id());
        assertEquals(123, branchTO.branchId());
        assertEquals("Main Branch", branchTO.branchName());
        assertEquals("Bangalore", branchTO.branchAddress());
    }

    @Test
    public void findById_whenBranchDetailsNotExist_thenThrowException() {
        when(branchMongoDbRepository.findById("123")).thenReturn(Optional.empty());

        assertThrows(BranchDetailsNotFoundException.class, () -> branchService.findById("123"));
    }

    @Test
    public void findByBranchName_whenBranchDetailsExist_thenReturnBranchData() throws BranchDetailsNotFoundException {
        Branch branch = new Branch();
        branch.setId("123");
        branch.setBranchId(123);
        branch.setBranchName("Main Branch");
        branch.setBranchAddress("Bangalore");

        when(branchMongoDbRepository.findByBranchName("Main Branch")).thenReturn(branch);

        BranchTO branchTO = branchService.findByBranchName("Main Branch");
        assertEquals("123", branchTO.id());
        assertEquals(123, branchTO.branchId());
        assertEquals("Main Branch", branchTO.branchName());
    }

    @Test
    public void findByBranchName_whenBranchDetailsNotExist_thenThrowException() {
        when(branchMongoDbRepository.findByBranchName("Main Branch")).thenReturn(null);

        assertThrows(BranchDetailsNotFoundException.class, () -> branchService.findByBranchName("Main Branch"));
    }

    @Test
    public void deleteById_whenBranchExists_thenDeleteSuccessfully() throws BranchDetailsNotFoundException {
        when(branchMongoDbRepository.existsById("123")).thenReturn(true);

        branchService.deleteById("123");

        verify(branchMongoDbRepository).deleteById("123");
    }

    @Test
    public void deleteById_whenBranchNotExist_thenThrowException() {
        when(branchMongoDbRepository.existsById("123")).thenReturn(false);

        assertThrows(BranchDetailsNotFoundException.class, () -> branchService.deleteById("123"));
    }

    @Test
    public void save_whenBranchDetailsAreValid_thenReturnSavedBranch() throws BranchDetailsNotFoundException {
        BranchRequest branchRequest = new BranchRequest();
        Branch branch = new Branch();
        branch.setId("123");
        branch.setBranchId(123);
        branch.setBranchName("Main Branch");
        branch.setBranchAddress("Bangalore");

        when(branchMongoDbRepository.save(any(Branch.class))).thenReturn(branch);

        BranchTO branchTO = branchService.save(branchRequest);

        assertEquals("123", branchTO.id());
        assertEquals(123, branchTO.branchId());
        assertEquals("Main Branch", branchTO.branchName());
    }

    @Test
    public void save_whenBranchNotSaved_thenThrowException() {
        BranchRequest branchRequest = new BranchRequest();

        when(branchMongoDbRepository.save(any(Branch.class))).thenReturn(null);

        assertThrows(BranchDetailsNotFoundException.class, () -> branchService.save(branchRequest));
    }

    @Test
    public void update_whenValidUpdateRequest_thenBranchIsUpdated() throws BranchDetailsNotFoundException {
        Branch branch = new Branch();
        branch.setId("123");
        branch.setBranchId(123);
        branch.setBranchName("Main Branch");
        branch.setBranchAddress("Bangalore");

        BranchUpdateRequest updateRequest = new BranchUpdateRequest();
        updateRequest.setId("123");
        updateRequest.setBranchId(456);
        updateRequest.setBranchName("Updated Branch");
        updateRequest.setBranchAddress("Mumbai");

        when(branchMongoDbRepository.findById("123")).thenReturn(Optional.of(branch));
        when(branchMongoDbRepository.save(branch)).thenReturn(branch);

        BranchTO branchTO = branchService.update(updateRequest);

        assertEquals("123", branchTO.id());
        assertEquals(456, branchTO.branchId());
        assertEquals("Updated Branch", branchTO.branchName());
        assertEquals("Mumbai", branchTO.branchAddress());
    }

    @Test
    public void update_whenBranchDoesNotExist_thenThrowException() {
        BranchUpdateRequest updateRequest = new BranchUpdateRequest();
        updateRequest.setId("123");
        updateRequest.setBranchId(456);
        updateRequest.setBranchName("Updated Branch");
        updateRequest.setBranchAddress("Mumbai");

        when(branchMongoDbRepository.findById("123")).thenReturn(Optional.empty());

        assertThrows(BranchDetailsNotFoundException.class, () -> branchService.update(updateRequest));
    }
}
