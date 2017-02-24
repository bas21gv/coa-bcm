CREATE DATABASE [COA_BCM]
GO
USE [COA_BCM]
GO
/****** Object:  Table [dbo].[BCM_Notification]    Script Date: 02/14/2017 13:52:42 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[BCM_Notification](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[Device_Id] [varchar](255) NOT NULL,
	[Topic] [varchar](50) NOT NULL,
	[Description] [varchar](255) NULL,
	[Conference_Details] [varchar](100) NOT NULL,
	[Conference_Date] [date] NOT NULL,
	[Conference_Time] [time](7) NOT NULL,
	[Is_Read] [int] NOT NULL,
 CONSTRAINT [PK_BCM_Notification] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[BCM_Device]    Script Date: 02/14/2017 13:52:42 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[BCM_Device](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[Device_Id] [varchar](255) NOT NULL,
 CONSTRAINT [PK_BCM_Device] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Default [DF_BCM_Notification_Is_Read]    Script Date: 02/14/2017 13:52:42 ******/
ALTER TABLE [dbo].[BCM_Notification] ADD  CONSTRAINT [DF_BCM_Notification_Is_Read]  DEFAULT ((0)) FOR [Is_Read]
GO