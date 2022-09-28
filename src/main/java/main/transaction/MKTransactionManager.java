package main.transaction;

import main.exceptions.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.transaction.*;

@Component
public class MKTransactionManager {
  @Autowired
  private JtaTransactionManager jtaTransactionManager;


  public void begin() {
    try {
      jtaTransactionManager.getTransactionManager().begin();
    } catch (NullPointerException | NotSupportedException | SystemException e) {
      System.out.println(e);
      System.out.println("Unexpected error while beginning transactions");
      throw new TransactionException("Непредвиденная ошибка при начале транзакции");
    }
  }

  public void commit() {
    try {
      jtaTransactionManager.getTransactionManager().commit();
    } catch (NullPointerException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SystemException e) {
      System.out.println(e);
      System.out.println("Unexpected error while committing transactions");


      try {
        jtaTransactionManager.getTransactionManager().rollback();
      } catch (SystemException ex) {
        System.out.println(ex);
        System.out.println("Can't rollback after failed commit");
        throw new TransactionException("Невозможно выполнить откат после неудачной поптыки завершить транзакцию");
      }

      throw new TransactionException("Непредвиденная ошибка при завершении транзакции");
    }
  }

  public void rollback() {
    try {
      jtaTransactionManager.getTransactionManager().rollback();
    } catch (NullPointerException | SystemException e) {
      System.out.println(e);
      System.out.println("Unexpected error while rolling back");
      throw new TransactionException("Неожиданная ошибка при откате");
    }
  }
}
