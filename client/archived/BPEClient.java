/* 
 *  ING-DiBa BPE-Wrapper
 * 
 *  Copyright (C) 2015 by ING-DiBa. All rights reserved.
 */
package com.redhat.ejb.server2server;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import org.jboss.logging.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ing.diba.bpe.AktivitaetsTyp;
import com.ing.diba.bpe.BpeConstants;
import com.ing.diba.bpe.api.ApplicationInfo;
import com.ing.diba.bpe.api.BPEAdapter;
import com.ing.diba.bpe.api.BPEAdapterException;
import com.ing.diba.bpe.api.BPEAdapterRuntimeException;
import com.ing.diba.bpe.api.BPEAdapterState;
import com.ing.diba.bpe.api.BPEAdapterState.State;
import com.ing.diba.bpe.api.BPEAdminAdapter;
import com.ing.diba.bpe.api.ProcessDefinitionVersionInfo;
import com.ing.diba.bpe.api.UserId;
import com.ing.diba.bpe.api.VorgangRecentlyUsed;
import com.ing.diba.bpe.api.deletion.DeleteProcessInstanceCriteria;
import com.ing.diba.bpe.api.exception.ExceptionId;
import com.ing.diba.bpe.api.process.AktivitaetInfo;
import com.ing.diba.bpe.api.process.ProcessInstanceId;
import com.ing.diba.bpe.api.process.ProcessInstanceInfo;
import com.ing.diba.bpe.api.task.TaskInfo;
import com.ing.diba.bpe.api.task.WorkItem;
import com.ing.diba.bpe.api.worklist.PagingValues;
import com.ing.diba.bpe.api.worklist.PagingWorkItemFilterValues;
import com.ing.diba.bpe.api.worklist.WorkItemHardFilterValues;
import com.ing.diba.bpe.api.worklist.WorkListPage;

public abstract class BPEClient {
  
  private final static Logger LOG = Logger.getLogger(BPEClient.class);
  
  protected Hashtable<?, ?> props;
  
  
  /////////////////////////////////////////////////////
  // AS-302
  // Make sure that the ejb client proxy for the bpe-wrapper-client will be reused,
  // So that the MwsRequestIdStickyClusterNodeSelector will be called for load-balancing
  // and failover within the cluster 'EJB'
  static protected BPEAdapter bpeAdapterCamunda = null;
  static protected BPEAdminAdapter bpeAdminAdapterCamunda = null;

  public BPEAdapter getBpeAdapterCAMUNDA() throws NamingException {
    if(bpeAdapterCamunda == null) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Creating new BPE-Wrapper client stub.");
      }
      bpeAdapterCamunda = BPEAdapterServiceFactory.createEjbRemotingBPEWrapper(props);
    }
    return bpeAdapterCamunda;
  }
  protected BPEClient(Hashtable<?, ?> props) {
	  this.props = new Properties();
	  }
  
  public BPEAdminAdapter getBpeAdminAdapterCAMUNDA() throws NamingException {
    if(bpeAdminAdapterCamunda == null) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Creating new BPE-Wrapper admin-client stub.");
      }
      bpeAdminAdapterCamunda = BPEAdapterServiceFactory.createEjbRemotingBPEAdminWrapper(props);
    }
    return bpeAdminAdapterCamunda;
  }
  
  protected BPEAdapter bpeAdapterFinden(Long relevantId) {
	 LOG.debug("Aufruf bpeAdapterFinden id :"+relevantId);
	  try {
          return this.getBpeAdapterCAMUNDA();
    } catch (NamingException e ) {
      throw new BPEAdapterRuntimeException(ExceptionId.SYSTEM_ERROR, e);
    }
  }
  
  protected BPEAdapter bpeAdapterFindenByEngineName(String engineName) {
	  LOG.debug("Aufruf bpeAdapterFinden name :"+engineName);
	  try {
          return this.getBpeAdapterCAMUNDA();
    } catch (NamingException e ) {
        throw new BPEAdapterRuntimeException(ExceptionId.SYSTEM_ERROR, e);
      }
  }
  
  protected BPEAdminAdapter bpeAdminAdapterFindenByEngineName(String engineName) {
	  LOG.debug("Aufruf bpeAdminAdapterFinden name :"+engineName);
	  try {
          return this.getBpeAdminAdapterCAMUNDA();
    } catch (NamingException e ) {
        throw new BPEAdapterRuntimeException(ExceptionId.SYSTEM_ERROR, e);
      }
  }
  
  protected BPEAdminAdapter bpeAdminAdapterFinden(Long relevantId) {
	  LOG.debug("Aufruf bpeAdminAdapterFinden id :"+relevantId);
	  try {
          return this.getBpeAdminAdapterCAMUNDA();
    } catch (NamingException e ) {
        throw new BPEAdapterRuntimeException(ExceptionId.SYSTEM_ERROR, e);
      }
  }
  
  protected ApplicationInfo provideApplicationInfo() {
	  try {
          return this.getBpeAdapterCAMUNDA().getApplicationInfo();
        } catch (Exception ex) {
          LOG.debug("Could not connect to BPE-Wrapper instance. ", ex);
        throw new BPEAdapterRuntimeException(ExceptionId.SYSTEM_ERROR, "BPE-Wrapper not available, giving up. ", ex);
        }
  }
  
  protected String provideDbName() {
	  try {
          return this.getBpeAdapterCAMUNDA().retrieveDbName();
        } catch (Exception ex) {
        	LOG.debug("Could not connect to BPE-Wrapper instance. ", ex);
        	throw new BPEAdapterRuntimeException(ExceptionId.SYSTEM_ERROR, "BPE-Wrapper not available, giving up. ", ex);
        }
  }
  
  protected void removeFachLoggingPartitions() throws BPEAdapterException {
	  try {
          this.getBpeAdminAdapterCAMUNDA().dropFachLoggingExpiredPartitions();
          return;
        } catch (Exception ex) {
        	LOG.debug("Could not connect to BPE-Wrapper instance. ", ex);
        	throw new BPEAdapterRuntimeException(ExceptionId.SYSTEM_ERROR, "BPE-Wrapper not available, giving up. ", ex);
        }
  }
  
  protected void newFachLoggingPartitions() throws BPEAdapterException {
	  try {
          this.getBpeAdminAdapterCAMUNDA().createFachLoggingPartitions();
          return;
        } catch (Exception ex) {
        	LOG.debug("Could not connect to BPE-Wrapper instance. ", ex);
        	throw new BPEAdapterRuntimeException(ExceptionId.SYSTEM_ERROR, "BPE-Wrapper not available, giving up. ", ex);
        }
  }
  
  protected Map<String, ProcessDefinitionVersionInfo> provideProcessDefinitionVersionInfo() {
	   Map<String, ProcessDefinitionVersionInfo> result = new HashMap<String, ProcessDefinitionVersionInfo>();
		  try {
		          result.putAll(this.getBpeAdapterCAMUNDA().retrieveProcessDefinitionVersions());
		        } catch (Exception ex) {
		        	LOG.debug("Could not connect to BPE-Wrapper instance. ", ex);
		        	throw new BPEAdapterRuntimeException(ExceptionId.SYSTEM_ERROR, "BPE-Wrapper not available, giving up. ", ex);
		        }
		        return result;
		  }
  
  protected ProcessDefinitionVersionInfo provideProcessDefinitionVersionInfo(String processName) {
	  Map<String, ProcessDefinitionVersionInfo> result = new HashMap<String, ProcessDefinitionVersionInfo>();
      try {
        result = this.getBpeAdapterCAMUNDA().retrieveProcessDefinitionVersions();
        if (result.containsKey(processName)) {
          return result.get(processName);
        }else
      	  return null;
      } catch (Exception ex) {
    	  LOG.debug("Could not connect to BPE-Wrapper instance. ", ex);
      	throw new BPEAdapterRuntimeException(ExceptionId.SYSTEM_ERROR, "BPE-Wrapper not available, giving up. ", ex);
      }
}
  protected BPEAdapterState provideBPEAdapterWrapperStates() {
	  BPEAdapterState result  = new BPEAdapterState(State.BPE_NOT_OK, "BPE Wrapper did not respond");;
      
      try {
        BPEAdapterState state = this.getBpeAdapterCAMUNDA().checkState();
        result.setBpeWrapperState(state.getState());
        result.setBpeWrapperMessage(state.getMessage());
      } catch (Exception e) {
        result.setBpeWrapperState(State.BPE_NOT_OK);
        result.setBpeWrapperMessage("BPE Wrapper did not respond");
      }
      return result;
  
}
  
  public List<WorkItem> provideAssignedWorkItems(UserId user, boolean includeWiedervorlage) {
	  try {
          // delegieren an camunda
          return this.getBpeAdapterCAMUNDA().retrieveAssignedWorkItems(user, includeWiedervorlage);
        } catch (Exception ex) {
        	 LOG.debug("Error retrieving BPEWrapper for Worklist. ", ex);
             throw new BPEAdapterRuntimeException(ExceptionId.SYSTEM_ERROR,
   	              "Error retrieving BPEWrapper for Worklist.",ex);
        }
  }
  
  public WorkListPage provideWorkListPage(UserId user, PagingWorkItemFilterValues filterValues,
      WorkItemHardFilterValues hardfilterValues, PagingValues pagingValues) {
	  try {
          // delegieren an camunda
          return this.getBpeAdapterCAMUNDA().retrieveWorkListPage(user, filterValues, hardfilterValues, pagingValues);
        } catch (Exception ex) {
          LOG.debug("Error retrieving BPEWrapper for Worklist. ", ex);
          throw new BPEAdapterRuntimeException(ExceptionId.SYSTEM_ERROR,
	              "Error retrieving BPEWrapper for Worklist.",ex);
        }
     
  }
  
  public int provideWorkListSize(UserId user, PagingWorkItemFilterValues filterValues, WorkItemHardFilterValues hardfilterValues) {
	  try {
          // delegieren an camunda
          return this.getBpeAdapterCAMUNDA().retrieveWorkListSize(user, filterValues, hardfilterValues);
        } catch (Exception ex) {
          LOG.debug("Error retrieving BPEWrapper for Worklist. ", ex);
          throw new BPEAdapterRuntimeException(ExceptionId.SYSTEM_ERROR,
	              "Error retrieving BPEWrapper for Worklist.",ex);
        }
  }
  
  public Map<String, Map<String, Long>> provideWorkItemStatistics(UserId user, WorkItemHardFilterValues hardfilterValues) {
	   try {
	          // delegieren an camunda
	          return this.getBpeAdapterCAMUNDA().retrieveWorkItemStatistics(user, hardfilterValues);
	        } catch (Exception ex) {
	          LOG.debug("Error retrieving BPEWrapper for Worklist. ", ex);
	          throw new BPEAdapterRuntimeException(ExceptionId.SYSTEM_ERROR,
		              "Error retrieving BPEWrapper for Worklist.",ex);
	        }
	  }
  
  protected List<TaskInfo> provideErrorTasks() throws BPEAdapterException {
	  List<TaskInfo> tasks = new ArrayList<TaskInfo>();
      try {
        tasks.addAll(this.getBpeAdminAdapterCAMUNDA().retrieveErrorTasks());
      } catch (Exception ex) {
        LOG.debug("Error retrieving error tasks from BPEWrapper. ", ex);
        throw new BPEAdapterRuntimeException(ExceptionId.SYSTEM_ERROR,
                "BPE-Wrapper not available, giving up.",ex);
      }
      return tasks;
}
  
  protected List<ProcessInstanceId> provideEndedPIs() throws BPEAdapterException {
	   List<ProcessInstanceId> result = new ArrayList<ProcessInstanceId>();
       try {
         result.addAll(this.getBpeAdminAdapterCAMUNDA().retrieveEndedPIs());
   
       } catch (Exception ex) {
         LOG.debug("Error retrieving ended process instances from BPEWrapper. ", ex);
         throw new BPEAdapterRuntimeException(ExceptionId.SYSTEM_ERROR,
             "BPE-Wrapper not available, giving up.",ex);
       }
       return result;
 }
  
  protected List<AktivitaetInfo> provideAktivitaeten(AktivitaetsTyp aktivitaetsTyp) {

      List<AktivitaetInfo> result = new ArrayList<AktivitaetInfo>();
      try {
        result.addAll(this.getBpeAdapterCAMUNDA().retrieveAktivitaeten(aktivitaetsTyp));
    
      } catch (Exception ex) {
    	  LOG.debug("Error retrieving ended process instances from BPEWrapper. ", ex);
          throw new BPEAdapterRuntimeException(ExceptionId.SYSTEM_ERROR,
              "BPE-Wrapper not available, giving up.",ex);
        
      }
      return result;
}
  
  protected void deleteProcessInstancesByCriteria(DeleteProcessInstanceCriteria query) throws BPEAdapterException {
	  try {
          this.getBpeAdminAdapterCAMUNDA().deleteProcessInstances(query);
        
        } catch (Exception ex) {
          LOG.warn("Error deleting process instances by criteria on BPEWrapper only scenario", ex);
		  throw new BPEAdapterRuntimeException(ExceptionId.SYSTEM_ERROR,"BPE-Wrapper not available, giving up.",ex);
        }
  }
  
  protected Long lowestProcessInstanceId(List<ProcessInstanceInfo> pis) {
    Collections.sort(pis, new ProcessInstanceInfoByProcessInstanceIdComparator()); // effizient nach ProcessInstanceID sortieren
    return pis.get(0).getProcessInstanceId().getId(); // kleinste zurueckgeben
  }
  
  protected List<VorgangRecentlyUsed> holeBearbeiteteVorgangsnummernFuerBenutzerUndTagWrapperDannAdapter(UserId userId, Date date) {
	  List<VorgangRecentlyUsed> result = null;
	     
      try {
        result = getBpeAdapterCAMUNDA().holeBearbeiteteVorgangsnummernFuerBenutzerUndTag(userId, date);
      } catch (Exception ex) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("Fehler beim Ho len der vorgangsnummer fuer den Benutzer: " + userId.getId()
              + "' from BPEWrapper instance only scenario", ex);
        }
        throw new BPEAdapterRuntimeException(ExceptionId.SYSTEM_ERROR,
                "BPE-Wrapper not available, giving up.",ex);
      }
    return result;
}
  
  private static class ProcessInstanceInfoByProcessInstanceIdComparator implements Comparator<ProcessInstanceInfo> {
    
    @Override
    public int compare(ProcessInstanceInfo o1, ProcessInstanceInfo o2) {
      return o1.getProcessInstanceId().getId().compareTo(o2.getProcessInstanceId().getId());
    }
    
  }
  
}
