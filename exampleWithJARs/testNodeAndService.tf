terraform {
  required_providers {
      azurerm = {
          source  = "hashicorp/azurerm"
          version = "~> 3.58.0"
      }
  }

  required_version = ">= 1.1.0"
}

provider "azurerm" {
  features {}
}

resource "azurerm_resource_group" "testRG" {
	name = "testarchimateRG"
	location = "West Europe"

	timeouts {
		create = "2h"

	}
}

resource "azurerm_storage_share" "testShare" {
	storage_account_name = azurerm_storage_account.testStorage.name
	quota = 50
	name = "myarchimateiacshare"

	acl {
		id = "MTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTI"

	}
}

resource "azurerm_storage_account" "testStorage" {
	resource_group_name = azurerm_resource_group.testRG.name
	name = "myarchimateiacstorage"
	location = "West Europe"
	account_tier = "Standard"
	account_replication_type = "LRS"

	timeouts {
		update = "2h"
		create = "1h"

	}
}

