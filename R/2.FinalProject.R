install.packages("ape")
install.packages('cluster')
install.packages('purrr')
install.packages('factoextra')

library(cluster)
library(purrr)
library(factoextra)
library(dendextend)

library("xlsx")
library("ape")

# Read the rows data from file
 dta <- read.xlsx(".//data-course.xlsx", sheetIndex = 1) 
#dta <- read.xlsx(".//data-entry.xlsx", sheetIndex = 1) 
data <- dta

data$Country <- as.numeric(dta$Country)
data$Continent <- as.numeric(dta$Continent)
data$Gender <- as.numeric(dta$Gender)
#data$Entry <- as.numeric(dta$Entry)

# manhattan, euclidean, minkowski
d <- dist(data,method = "manhattan")

# simple/(single), complete, average, ward.D, ward.D2, centroid
hc <- hclust(d, method = "single")

cc <- agnes(data, method = "single")

cc

## Agglomerative coefficient:  0.993762


################
plot(hc, hang = -1, cex = 0.5,
     main = "Cluster dendrogram")
################
plot(as.phylo(hc), cex = 0.6, label.offset = 0.1)
################
# Unrooted
plot(as.phylo(hc), type = "unrooted", cex = 0.6,
     no.margin = TRUE)
###############
# Cut the dendrogram into 4 clusters
colors = c("red", "blue", "green", "black")
clus4 = cutree(hc, 4)
plot(as.phylo(hc), type = "fan", tip.color = colors[clus4],
     label.offset = 0.1, cex = 0.7)
###############
# Change the appearance
# change edge and label (tip)
plot(as.phylo(hc), type = "cladogram", cex = 0.6,
     edge.color = "steelblue", edge.width = 2, edge.lty = 2,
     tip.color = "steelblue")
###############
#ggplot

d <- dist(data, method = "minkowski")
hc1 <- hclust(d, method = "average" )

plot(hc1, cex = 0.6, hang = -1)

m <- c( "average", "single", "complete", "ward")
names(m) <- c( "average", "single", "complete", "ward")

ac <- function(x) {
  agnes(data, method = x)$ac
}

map_dbl(m, ac)  
?agnes
hc3 <- agnes(data, method = "average", metric = "minkowski")

clust <- cutree(hc3, k = 3)
fviz_cluster(list(data = data, cluster = clust))

pltree(hc3, cex = 0.6, hang = -1, main = "Dendrogram of agnes")
rect.hclust(hc3, k = 3, border = 2:10)

hc_single <- agnes(data, method = "average")
hc_complete <- agnes(data, method = "ward")

# converting to dendogram objects as dendextend works with dendogram objects 
hc_single <- as.dendrogram(hc_single)
hc_complete <- as.dendrogram(hc_complete)

tanglegram(hc_single,hc_complete)
