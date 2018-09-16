library(tidyverse)
library(magrittr)

remove(list=ls())

# FUNCTIONS ---------------------------------------------------------------------------------------
# Calculate confidence interval
calculate_confidence <- function(x){
  x * 1.96 / sqrt(100)
}

# Function calculate Breach Rate ------------------------------------------------------------------
calculate_breach <- function(x){
  x %<>% mutate(Value = RateFBreachC_A,
                Min = RateFBreachC_A_Min,
                Max = RateFBreachC_A_Max) %>% 
    select(Step, Value, Min, Max, Contractor)
  x
}

# Function calculate Trust ------------------------------------------------------------------------
calculate_trust <- function(x){
  x %<>% mutate(Value = TrustFToC_A,
                Min = TrustFToC_A_Min,
                Max = TrustFToC_A_Max) %>% 
    select(Step, Value, Min, Max, Contractor)
  x
}

# Function calculate Volume -----------------------------------------------------------------------
calculate_vol <- function(x){
  x %<>% mutate(Value = VolC_A / 1000,
                Min = VolC_A_Min / 1000,
                Max = VolC_A_Max / 1000) %>% 
    select(Step, Value, Min, Max, Contractor)
  x
}

# Function calculate Payoffs ----------------------------------------------------------------------
calculate_payoffs <- function(x){
  x %<>% mutate(Value = PayoffsC_A / 1000,
                Min = PayoffsC_A_Min / 1000,
                Max = PayoffsC_A_Max / 1000) %>% 
    select(Step, Value, Min, Max, Contractor)
  x
}

# Function to re-factor the contractor variable in all the final tibbles
refactor_contractor <- function(x, rice_default_rate){
  x %<>% mutate(Contractor = Contractor %>% factor() %>% 
                  fct_relevel("20% Rate, Committed",
                              paste0(rice_default_rate * 100, "% Rate, 10% Breach"),
                              "10% Rate, Committed",
                              paste0(rice_default_rate * 100, "% Rate, 30% Breach"),
                              "5% Rate, Committed",
                              "Spot Market"))
  x
}

# Export relevant plots ---------------------------------------------------------------------------
export_plots <- function(dataB00, dataB10, dataB30, rice_default_rate, total_steps, min_step, rice_type){
  
  # Add Breach attribute to differentite outputs
  dataB00 %<>% mutate(Breach = 0)
  dataB10 %<>% mutate(Breach = 10)
  dataB30 %<>% mutate(Breach = 30)
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
  
  # Round Exp values
  data$Exp <- round(data$Exp, 2)
  
  # Select relevant variables
  data %<>% select(Breach, Exp, Step, 
                   starts_with("Vol"), 
                   starts_with("Payoffs"),
                   starts_with("TrustFToC"),
                   starts_with("RateFBreachC"))
  
  # If Jasmine rice re-filter to max_steps = 9
  if (total_steps == 9) 
    data %<>% filter(Step %% 2 == 0) %>% 
    mutate(Step = Step / 2)
  # Step plus 1 to remove Step = 0
  data %<>% mutate(Step = Step + 1)
  
  # Filter into sub-tibbles
  Rate_05_Breach_00 <- data %>% filter(Exp == 0.05, Breach == 0) %>% 
    mutate(Contractor = "5% Rate, Committed")
  Rate_10_Breach_00 <- filter(data, Exp == 0.1, Breach == 0) %>% 
    mutate(Contractor = "10% Rate, Committed")
  Rate_20_Breach_00 <- filter(data, Exp == 0.2, Breach == 0) %>% 
    mutate(Contractor = "20% Rate, Committed")
  Rate_Default_Breach_10 <- filter(data, Exp == rice_default_rate, Breach == 10) %>% 
    mutate(Contractor = paste0(rice_default_rate * 100, "% Rate, 10% Breach"))
  Rate_Default_Breach_30 <- filter(data, Exp == rice_default_rate, Breach == 30) %>% 
    mutate(Contractor = paste0(rice_default_rate * 100, "% Rate, 30% Breach"))
  
  # Calculate relevant variables ------------------------------------------------------------------
  # Breach Rate
  Breach_Rate_05_Breach_00 <- calculate_breach(Rate_05_Breach_00)
  Breach_Rate_10_Breach_00 <- calculate_breach(Rate_10_Breach_00)
  Breach_Rate_20_Breach_00 <- calculate_breach(Rate_20_Breach_00)
  Breach_Rate_Default_Breach_10 <- calculate_breach(Rate_Default_Breach_10)
  Breach_Rate_Default_Breach_30 <- calculate_breach(Rate_Default_Breach_30)
  
  Breach_Spot_Market <- tibble(Step = Rate_05_Breach_00$Step, 
                               Value = 0, Min = 0, Max = 0, 
                               Contractor = "Spot Market")	
  
  Breach_Final <- bind_rows(Breach_Rate_20_Breach_00,
                            Breach_Rate_Default_Breach_10,
                            Breach_Rate_10_Breach_00,
                            Breach_Rate_05_Breach_00,
                            Breach_Rate_Default_Breach_30,
                            Breach_Spot_Market)
  
  # Trust Rate
  Trust_Rate_05_Breach_00 <- calculate_trust(Rate_05_Breach_00)
  Trust_Rate_10_Breach_00 <- calculate_trust(Rate_10_Breach_00)
  Trust_Rate_20_Breach_00 <- calculate_trust(Rate_20_Breach_00)
  Trust_Rate_Default_Breach_10 <- calculate_trust(Rate_Default_Breach_10)
  Trust_Rate_Default_Breach_30 <- calculate_trust(Rate_Default_Breach_30)
  
  Trust_Spot_Market <- tibble(Step = Rate_05_Breach_00$Step, 
                              Value = 0.9, Min = 0.9, Max = 0.9, Contractor = "Spot Market")	
  
  Trust_Final <- bind_rows(Trust_Rate_20_Breach_00,
                           Trust_Rate_Default_Breach_10,
                           Trust_Rate_10_Breach_00,
                           Trust_Rate_05_Breach_00,
                           Trust_Rate_Default_Breach_30,
                           Trust_Spot_Market)
  
  # Volume
  Vol_Rate_05_Breach_00 <- calculate_vol(Rate_05_Breach_00)
  Vol_Rate_10_Breach_00 <- calculate_vol(Rate_10_Breach_00)
  Vol_Rate_20_Breach_00 <- calculate_vol(Rate_20_Breach_00)
  Vol_Rate_Default_Breach_10 <- calculate_vol(Rate_Default_Breach_10)
  Vol_Rate_Default_Breach_30 <- calculate_vol(Rate_Default_Breach_30)
  
  Vol_Spot_Market <- tibble(Step = Rate_05_Breach_00$Step, 
                            Value = 4, Min = 4, Max = 4, Contractor = "Spot Market")
  
  Vol_Final <- bind_rows(Vol_Rate_20_Breach_00,
                         Vol_Rate_Default_Breach_10,
                         Vol_Rate_10_Breach_00,
                         Vol_Rate_05_Breach_00,
                         Vol_Rate_Default_Breach_30,
                         Vol_Spot_Market)
  
  # Payoffs
  Payoffs_Rate_05_Breach_00 <- calculate_payoffs(Rate_05_Breach_00)
  Payoffs_Rate_10_Breach_00 <- calculate_payoffs(Rate_10_Breach_00)
  Payoffs_Rate_20_Breach_00 <- calculate_payoffs(Rate_20_Breach_00)
  Payoffs_Rate_Default_Breach_10 <- calculate_payoffs(Rate_Default_Breach_10)
  Payoffs_Rate_Default_Breach_30 <- calculate_payoffs(Rate_Default_Breach_30)
  
  Payoffs_Spot_Market <- tibble(Step = Rate_05_Breach_00$Step, 
                                Value = Rate_05_Breach_00$PayoffsWithoutCF/1000, 
                                Min = Value, Max = Value,  Contractor = "Spot Market")	
  
  Payoffs_Final <- bind_rows(Payoffs_Rate_20_Breach_00,
                             Payoffs_Rate_Default_Breach_10,
                             Payoffs_Rate_10_Breach_00,
                             Payoffs_Rate_05_Breach_00,
                             Payoffs_Rate_Default_Breach_30,
                             Payoffs_Spot_Market)
  
  # Re-factor all variable tibbles
  Breach_Final <- refactor_contractor(Breach_Final, rice_default_rate)
  Trust_Final <- refactor_contractor(Trust_Final, rice_default_rate)
  Vol_Final <- refactor_contractor(Vol_Final, rice_default_rate)
  Payoffs_Final <- refactor_contractor(Payoffs_Final, rice_default_rate)
  
  # plot relevant variables -----------------------------------------------------------------------
  p1 <- ggplot(Breach_Final, aes(x = Step, y = Value, ymin = Min, ymax= Max, color = Contractor)) + 
    geom_point(aes(fill = Contractor)) + 
    geom_smooth(stat = "identity", aes(fill = Contractor), alpha = 0.23, linetype="dashed", size = 0.6) +
    theme_bw() + 
    theme(axis.text =  element_text(size=20), 
          axis.title = element_text(size=22, face="bold"), 
          legend.text = element_text(size=18), 
          legend.position="top", legend.title=element_blank(),
          plot.title = element_text(hjust=0.5, size=35, face="bold", margin=margin(0,0,20,0)),
          plot.margin = unit(c(0,ifelse(total_steps==18,1,0),0,0),"cm")) + 
    scale_y_continuous(name="Rate of Farmer Breaches (%)",
                       breaks = seq(0, 25, by = 5), limits = c(0, 26)) + 
    scale_x_continuous(breaks = seq(0,total_steps,min_step), name="cropping seasons") +
    ggtitle(rice_type)
  
  p2 <- ggplot(Trust_Final, aes(x = Step, y = Value, ymin = Min, ymax= Max, color = Contractor)) + 
    geom_point(aes(fill = Contractor)) + 
    geom_smooth(stat = "identity", aes(fill = Contractor), alpha = 0.23, linetype="dashed", size = 0.6) +
    theme_bw() + 
    theme(axis.text =  element_text(size=20), axis.title = element_text(size=22, face="bold"), 
          legend.text = element_text(size=18), legend.position="top", legend.title=element_blank(),
          plot.margin = unit(c(0,ifelse(total_steps==18,1,0),0,0),"cm")) + 
    scale_y_continuous(breaks = seq(0.1,0.9,0.2), name="Farmer's Trust in Contractor") + 
    scale_x_continuous(breaks = seq(0,total_steps,min_step), name="cropping seasons")
  
  p3 <- ggplot(Vol_Final, aes(x = Step, y = Value, ymin = Min, ymax= Max, color = Contractor)) + 
    geom_point(aes(fill = Contractor)) + 
    geom_smooth(stat = "identity", aes(fill = Contractor), alpha = 0.23, linetype="dashed", size = 0.6) +
    theme_bw() + 
    theme(axis.text =  element_text(size=20), axis.title = element_text(size=22, face="bold"), 
          legend.text = element_text(size=18), legend.position="top", legend.title=element_blank(),
          plot.margin = unit(c(0,ifelse(total_steps==18,1,0),0,0),"cm")) + 
    scale_y_continuous(name="Rice volume for export (thousand tons)",
                       breaks = seq(0, 4.0, by = 1.0), limits = c(0, 4.0)) + 
    scale_x_continuous(breaks = seq(0,total_steps,min_step), name="cropping seasons")
  
  p4 <- ggplot(Payoffs_Final, aes(x = Step, y = Value, ymin = Min, ymax= Max, color = Contractor)) + 
    geom_point(aes(fill = Contractor)) + 
    geom_smooth(stat = "identity", aes(fill = Contractor), alpha = 0.23, linetype="dashed", size = 0.6) +
    theme_bw() + 
    theme(axis.text =  element_text(size=20), axis.title = element_text(size=22, face="bold"), 
          legend.text = element_text(size=18), legend.position="top", legend.title=element_blank(),
          plot.margin = unit(c(0,ifelse(total_steps==18,1,0),0,0),"cm")) + 
    scale_y_continuous(name="Contractors' profit (billion VND)",
                       breaks = seq(0, 15, by = 5), limits = c(-3.3, 15)) + 
    scale_x_continuous(breaks = seq(0,total_steps,min_step), name="cropping seasons") + 
    geom_hline(yintercept=0, linetype="dashed", color = "black", size = 0.2)
  
  # Return list of relevant plots
  list(p1, p2, p3, p4)
}

# READ DATA ---------------------------------------------------------------------------------------
dataB00 <- read_delim("ConFar_AllSteps_Ordinary_Breach_00.txt", delim = ";", col_names = FALSE)
dataB10 <- read_delim("ConFar_AllSteps_Ordinary_Breach_10.txt", delim = ";", col_names = FALSE)
dataB30 <- read_delim("ConFar_AllSteps_Ordinary_Breach_30.txt", delim = ";", col_names = FALSE)
# Plots for ordinary rice
ord_plots <- export_plots(dataB00, dataB10, dataB30, 0.2, 18, 2, "Ordinary Rice")

dataB00 <- read_delim("ConFar_AllSteps_Jasmine_Breach_00.txt", delim = ";", col_names = FALSE)
dataB10 <- read_delim("ConFar_AllSteps_Jasmine_Breach_10.txt", delim = ";", col_names = FALSE)
dataB30 <- read_delim("ConFar_AllSteps_Jasmine_Breach_30.txt", delim = ";", col_names = FALSE)
# Plots for jasmine rice
jas_plots <- export_plots(dataB00, dataB10, dataB30, 0.1, 9, 1, "Jasmine Rice")

library(cowplot)
plot_grid(ord_plots[[1]],jas_plots[[1]],
          ord_plots[[2]],jas_plots[[2]],
          ord_plots[[3]],jas_plots[[3]],
          ord_plots[[4]],jas_plots[[4]],
          nrow=4,align="hv")
ggsave("timeSeriesGroupPlots.png", units="in", width=20, height=28, dpi=150)
