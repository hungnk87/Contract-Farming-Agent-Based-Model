library(tidyverse)
library(magrittr)
library(stringr)

remove(list=ls())

# Function to export heatmaps
export_plots <- function(dataB00, dataB10, dataB30, rice_type){

  # Add Breach attribute to differentite outputs
  dataB00 %<>% mutate(Breach = "breach00")
  dataB10 %<>% mutate(Breach = "breach10")
  dataB30 %<>% mutate(Breach = "breach30")
  total <- bind_rows(dataB00, dataB10, dataB30)
  
  # Extract names 
  row1 <- unlist(total[1,])
  names <- c()
  for(i in 1:(length(row1))){
    if (i %% 2 == 1) names[(i + 1) / 2] = row1[i] 
    if (i == length(row1)) names[(i + 1) / 2] = "Breach"
  }
  
  # Update names to output tibbles
  data <- tibble(Exp = unlist(total[,2])) # Firstly add "Exp" into the tibble
  # Then bind_cols could be performed without error
  for(i in 4:ncol(total)){
    if (i %% 2 == 0 | i == ncol(total)) data <- bind_cols(data, total[,i])
  }
  colnames(data) <- names
  
  # If Jasmine rice re-filter Steps
  if (rice_type == "Jasmine") 
    data %<>% filter(Step %% 2 == 0)
  
  # Calculate and select relevant variables
  data %<>% mutate(Exp = round(Exp, 2),
                   CFRate = paste0("rate", ifelse(Exp * 100 < 10, paste0("0", Exp * 100), Exp * 100))) %>% 
    select(Breach, CFRate, PayoffsC_A, PayoffsWithoutCF, VolC_A) %>% 
    group_by(Breach, CFRate) %>% 
    summarize(MeanPayoffsC = mean(PayoffsC_A), 
              MeanPayoffsSM = mean(PayoffsWithoutCF),
              Vol = round(mean(VolC_A) / 4000, 2)) %>% 
    mutate(PayoffsRate = round(MeanPayoffsC / MeanPayoffsSM, 2))
  
  library(RColorBrewer)
  hm.palette <- colorRampPalette(rev(brewer.pal(11, 'Spectral')), space='Lab')
  
  # Heatmap of average payoffs rate per crop in comparison with SM
  p1 <- ggplot(data, aes(x = CFRate, y = Breach, fill = PayoffsRate)) + 
      geom_tile() + coord_equal() + 
      scale_fill_gradientn(colours = hm.palette(100), breaks=seq(0,2,1), limits=c(0,2)) + 
      geom_text(aes(CFRate, Breach, label = PayoffsRate), color = "black", size = 8) + 
      theme(axis.text =  element_text(size=17), axis.title = element_text(size=22, face="bold"), 
            legend.text = element_text(size=18),
            plot.title = element_text(hjust=0.5, size=30, face="bold", margin=margin(0,0,20,0)),
            plot.margin = unit(c(0,ifelse(rice_type == "Ordinary", 1, 0),0,0),"cm")) +
      ylab("Commitment") + xlab("Contract Farming Rate") + labs(fill = "") + 
      ggtitle(paste0(ifelse(rice_type == "Ordinary", "Ordinary", "Jasmine")," Rice - Profit"))
  
  # Heatmap of average volume
  v1 <- ggplot(data, aes(x = CFRate, y = Breach, fill = Vol)) + 
      geom_tile() + coord_equal() +
      scale_fill_gradient2(low="white", high="brown", breaks=seq(0,1), limits=c(0,1)) +
      geom_text(aes(CFRate, Breach, label = Vol), color = "black", size = 8) + 
      theme(axis.text =  element_text(size=17), axis.title = element_text(size=22, face="bold"), 
          legend.text = element_text(size=18),
          plot.title = element_text(hjust=0.5, size=30, face="bold", margin=margin(0,0,20,0)),
          plot.margin = unit(c(0,ifelse(rice_type == "Ordinary", 1, 0),0,0),"cm")) +
      ylab("Commitment") + xlab("Contract Farming Rate") + labs(fill = "") + 
      ggtitle(paste0(ifelse(rice_type == "Ordinary", "Ordinary", "Jasmine"), " Rice - Volume"))
  
  # Return list of relevant plots
  list(p1, v1)
}

dataB00 <- read_delim("ConFar_AllSteps_Ordinary_Breach_00.txt", delim = ";", col_names = FALSE)
dataB10 <- read_delim("ConFar_AllSteps_Ordinary_Breach_10.txt", delim = ";", col_names = FALSE)
dataB30 <- read_delim("ConFar_AllSteps_Ordinary_Breach_30.txt", delim = ";", col_names = FALSE)
ord_plots <- export_plots(dataB00, dataB10, dataB30, "Ordinary")

dataB00 <- read_delim("ConFar_AllSteps_Jasmine_Breach_00.txt", delim = ";", col_names = FALSE)
dataB10 <- read_delim("ConFar_AllSteps_Jasmine_Breach_10.txt", delim = ";", col_names = FALSE)
dataB30 <- read_delim("ConFar_AllSteps_Jasmine_Breach_30.txt", delim = ";", col_names = FALSE)
jas_plots <- export_plots(dataB00, dataB10, dataB30, "Jasmine")

library(cowplot)
plot_grid(ord_plots[[1]],jas_plots[[1]],
          ord_plots[[2]],jas_plots[[2]],
          nrow=2,rel_widths = c(7.8,6))
ggsave("heatMapFull.png", units="in", width=20, height=12, dpi=150)

dataB00 <- read_delim("ConFar_AllSteps_WE_Ordinary_Breach_00.txt", delim = ";", col_names = FALSE)
dataB10 <- read_delim("ConFar_AllSteps_WE_Ordinary_Breach_10.txt", delim = ";", col_names = FALSE)
dataB30 <- read_delim("ConFar_AllSteps_WE_Ordinary_Breach_30.txt", delim = ";", col_names = FALSE)
ord_plots <- export_plots(dataB00, dataB10, dataB30, "Ordinary")

dataB00 <- read_delim("ConFar_AllSteps_WE_Jasmine_Breach_00.txt", delim = ";", col_names = FALSE)
dataB10 <- read_delim("ConFar_AllSteps_WE_Jasmine_Breach_10.txt", delim = ";", col_names = FALSE)
dataB30 <- read_delim("ConFar_AllSteps_WE_Jasmine_Breach_30.txt", delim = ";", col_names = FALSE)
jas_plots <- export_plots(dataB00, dataB10, dataB30, "Jasmine")

plot_grid(ord_plots[[1]],jas_plots[[1]],
          nrow=1,rel_widths = c(7.8,6))
ggsave("heatMapFullWE.png", units="in", width=20, height=6, dpi=150)
