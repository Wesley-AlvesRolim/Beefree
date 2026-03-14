package com.wesley.beefree.data.apps

val BRAZILIAN_BANK_PACKAGE_NAMES: Set<String> =
    setOf(
        // Big Banks
        "com.nu.production", // Nubank
        "com.itau", // Itaú
        "com.itau.personnalite", // Itaú Personnalité
        "com.itau.empresas", // Itaú Empresas
        "br.com.itau.iti", // iti (Itaú)
        "br.com.itau.playersbank", // Player's Bank (Itaú)
        "com.bradesco", // Bradesco
        "br.com.bradesco.next", // Next (Bradesco)
        "br.com.bradesco.netempresa", // Bradesco Net Empresa
        "br.com.bradesco.cartoes", // Bradesco Cartões
        "br.com.bb.android", // Banco do Brasil
        "br.com.bb.investimentos", // BB Investimentos
        "br.com.bb.pjmobi", // BB Empresas
        "br.com.santander.usuarios", // Santander
        "br.com.santander.way", // Santander Way (Cartões)
        "br.com.santander.investimentos", // Santander Investimentos
        "br.com.caixa.mobile.apps", // CAIXA
        "br.gov.caixa.tem", // CAIXA Tem
        "br.com.caixa.mobi", // CAIXA (Outra versão)
        // Digital Banks & Fintechs
        "br.com.intermedium", // Inter
        "br.com.intermedium.pj", // Inter Empresas
        "com.c6bank.app", // C6 Bank
        "br.com.neon", // Neon
        "br.com.bancopan", // Banco PAN
        "br.com.agipag.app", // Agibank
        "br.com.willbank.app", // Will Bank
        "br.com.digio", // Digio
        "br.com.uol.ps.subscriber", // PagBank (PagSeguro)
        "com.picpay", // PicPay
        "com.mercadopago.wallet", // Mercado Pago
        "br.com.amedigital", // Ame Digital
        "com.recargapay", // RecargaPay
        "com.bitz", // Bitz (Bradesco)
        "br.com.banqi", // banQi (Casas Bahia)
        "br.com.superdigital.br", // Superdigital (Santander)
        "com.z1.z1app", // Z1
        "com.ngcash.app", // NG.CASH
        "br.com.altbank", // alt.bank
        "br.com.bancobs2.app", // BS2
        "br.com.bancobari.app", // Banco Bari
        "br.com.original.mobile.bancopessoal", // Banco Original
        "com.bancooriginal", // Banco Original (Outra versão)
        "com.btg.pactual.banking", // BTG Pactual Banking
        "com.btg.pactual.investimentos", // BTG Pactual Investimentos
        "br.com.bv.bancobv", // Banco BV
        "br.com.dmcard.app", // Banco DM
        // Regional & Specialized Banks
        "br.com.banrisul.banrisuldigital", // Banrisul
        "br.com.banrisul.mobi", // Banrisul (Outra versão)
        "br.com.banestes.mobi", // Banestes
        "br.com.brb.mobi", // BRB
        "br.com.banese.mobi", // Banese
        "br.com.banpara.mobi", // Banpará
        "br.com.basa.mobi", // Banco da Amazônia
        "br.com.bnb.mobile", // Banco do Nordeste
        "br.com.sicredi.mobile.pr", // Sicredi
        "br.com.sicoob.mobiservice", // Sicoob
        "br.com.sicoob.smf", // Sicoob (Outra versão)
        "br.com.uniprime.mobile", // Uniprime
        "br.com.ailos.mobile", // Ailos
        // Investment & Brokerage
        "br.com.xp.investimentos", // XP Investimentos
        "br.com.rico.investimentos", // Rico
        "br.com.clear.corretora", // Clear
        "br.com.toroinvestimentos", // Toro Investimentos
        "br.com.easynvest.main", // NuInvest (Easynvest)
        "com.warren.android", // Warren
        "com.vitreo.android", // Vitreo
        "com.orama.android", // Órama
        "com.guide.investimentos", // Guide
        "com.ativa.investimentos", // Ativa
        "br.com.genialinvestimentos.app", // Genial
        "br.com.modalmais.investimentos", // Modalmais
        "com.avenue.app", // Avenue
        "com.nomad.app", // Nomad
        "com.transferwise.android", // Wise
        "com.westernunion.androidapp.brazil", // Western Union
        // Crypto & Digital Assets
        "com.binance.dev", // Binance
        "com.coinbase.android", // Coinbase
        "com.crypto.stack.android", // Crypto.com
        "br.com.mercadobitcoin.android", // Mercado Bitcoin
        "com.foxbit.android", // Foxbit
        "com.bitso.bitso", // Bitso
        "com.bipa.app", // Bipa
        "com.ripio.ripio", // Ripio
        // Specialized Banks & Finance
        "br.com.daycoval.mobi", // Daycoval
        "br.com.sofisadireto.android", // Sofisa Direto
        "br.com.bancomaster.pf", // Banco Master
        "br.com.bancoabc.mobi", // Banco ABC Brasil
        "br.com.pine.pinebank", // Banco Pine
        "br.com.mercantil.mobi", // Mercantil do Brasil
        "br.com.safra.pf", // Safra PF
        "com.safra.android", // Safra (Outra versão)
        "com.safra.safrapay", // SafraPay
        "com.bmg.conta.digital", // Banco BMG
        "br.com.bancobmg.bancodigital", // Banco BMG (Outra versão)
        "br.com.rendimento.mobi", // Banco Rendimento
        "br.com.bancotopazio.mobi", // Banco Topázio
        "br.com.voiter.mobi", // Voiter
        "br.com.bancoinbursa.mobi", // Banco Inbursa
        // Benefits & Vouchers
        "br.com.alelo.meualelo", // Alelo
        "com.sodexo.br.mobile", // Sodexo/Pluxee
        "br.com.ticket.android", // Ticket
        "br.com.vr.android.vr_e_voce", // VR e Você
        "com.swile.swile", // Swile
        "com.flashbeneficios", // Flash
        "com.caju.android", // Caju
        // Credit & Score
        "br.com.serasaexperian.consumidor", // Serasa
        "br.com.boavista.consumidorpositivo", // Boa Vista
    )
