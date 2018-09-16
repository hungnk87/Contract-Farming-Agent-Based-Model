# README # 

### Source code associated to the paper "Contract farming in the Mekong Delta's rice supply chain: Insights from an aggent-based modeling study" submitted to JASSS journal

We study three obstacles of the expansion of contract rice farming in the Mekong Delta (MKD) region. The failure of buyers in building trust-based relationship with small-holder farmers, unattractive offered prices from the contract farming scheme, and limited rice processing capacity have constrained contractors from participating in the large-scale paddy field program. We present an agent-based model to examine the viability of contract farming in the region from the contractor perspective. The model focuses on financial incentives and trust, which affect the decision of relevant parties on whether to participate and honor a contract. The model is also designed in the context of the MKD’s rice supply chain with two contractors engaging in the contract rice farming scheme alongside an open market, in which both parties can renege on the agreement. We then evaluate the contractors’ performances with different combinations of scenarios related to the three obstacles. Our results firstly show that a fully-equipped contractor who opportunistically exploits a relatively small proportion (less than 10%) of the contracted farmers in most instances can outperform spot market-based contractors in terms of average profit achieved for each crop. Secondly, a committed contractor who offers lower purchasing prices than the most typical rate can obtain better earnings per ton of rice as well as higher profit per crop. However, those contractors in both cases could not enlarge their contract farming scheme, since either farmers’ trust toward them decreases gradually or their offers are unable to compete with the benefits from a competitor or the spot market. Thirdly, the results are also in agreement with the existing literature that the contract farming scheme is not a cost-effective method for buyers with limited rice processing capacity, which is a common situation among the contractors in the MKD region.

### How to run the model 

- Import the **ContractFarmingJASSS** folder to a Java IDE such as Eclipse or IntelliJ
- Load all jar files in **libraries** folder
- Config **agruments** to run the model from main class: view.ConFar_ConsoleSimulation 
  + -paramsFile ./config/Ordinary_Breach_00.properties -outputFile Ordinary_Breach_00
  + Sensitivity Analysis is currently set TRUE
- Outputs will then be saved to **logs** folder: ConFar_AllSteps_Ordinary_Breach_00
- Relevant output files will then be analyzed with **R codes** (uploaded under the same projects): 
  + heatMapCF.R
  + timeSeriesGroupPlots.R
  
  
### If you have any questions, please email to Hung at hung.nguyenkhanh@uon.edu.au
  
# THANK YOU FOR READING!!! #
